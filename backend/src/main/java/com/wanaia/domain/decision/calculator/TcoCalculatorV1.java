package com.wanaia.domain.decision.calculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.model.*;
import com.wanaia.domain.decision.util.SnapshotHasher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class TcoCalculatorV1 {

    private final ObjectMapper objectMapper;

    public TcoCalculatorV1(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TcoOutput calculate(TcoInput input) {
        if (input == null || input.purchasePrice() == null || input.marketContext() == null) {
            throw new IllegalArgumentException("TcoInput, purchasePrice, and marketContext must not be null");
        }

        MarketContext market = input.marketContext();
        BigDecimal price = input.purchasePrice();
        int annualMileage = (input.annualMileageKm() > 0) ? input.annualMileageKm() : 15000;
        double consumption = (input.fuelConsumptionMetric() != null && input.fuelConsumptionMetric() > 0)
            ? input.fuelConsumptionMetric()
            : 6.0;

        List<ScoreExplanationItem> explanations = new ArrayList<>();
        Map<Integer, TcoOutput.TcoHorizonBreakdown> horizons = new LinkedHashMap<>();

        int[] horizonYearsArray = {1, 3, 5};

        for (int years : horizonYearsArray) {
            // 1. Depreciation (Residual Value Curves: 1yr = 82%, 3yr = 65%, 5yr = 50%)
            BigDecimal retentionPct;
            if (years == 1) {
                retentionPct = BigDecimal.valueOf(0.82);
            } else if (years == 3) {
                retentionPct = BigDecimal.valueOf(0.65);
            } else {
                retentionPct = BigDecimal.valueOf(0.50);
            }

            BigDecimal residualValue = price.multiply(retentionPct).setScale(2, RoundingMode.HALF_UP);
            BigDecimal depreciation = price.subtract(residualValue).setScale(2, RoundingMode.HALF_UP);

            // 2. Fuel / Energy Cost
            BigDecimal unitPrice;
            if ("BEV".equalsIgnoreCase(input.propulsionType())) {
                unitPrice = market.electricityPricePerKwh();
            } else if ("DIESEL".equalsIgnoreCase(input.propulsionType())) {
                unitPrice = market.fuelPriceDieselPerLiter();
            } else {
                unitPrice = market.fuelPricePetrolPerLiter();
            }

            BigDecimal totalKm = BigDecimal.valueOf((long) annualMileage * years);
            BigDecimal energyUnitsConsumed = totalKm
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(consumption));

            BigDecimal energyCost = energyUnitsConsumed.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);

            // 3. Maintenance Cost (Annual routine servicing + wear & tear)
            BigDecimal annualRoutineMaintenance = price.multiply(BigDecimal.valueOf(0.012)); // 1.2% of MSRP
            if ("BEV".equalsIgnoreCase(input.propulsionType())) {
                annualRoutineMaintenance = annualRoutineMaintenance.multiply(BigDecimal.valueOf(0.60)); // EV 40% lower maintenance
            }
            BigDecimal maintenanceCost = annualRoutineMaintenance
                .multiply(BigDecimal.valueOf(years))
                .setScale(2, RoundingMode.HALF_UP);

            // 4. Insurance Cost (Comprehensive)
            BigDecimal annualInsurance = price.multiply(market.defaultAnnualInsuranceRatePct());
            BigDecimal insuranceCost = annualInsurance
                .multiply(BigDecimal.valueOf(years))
                .setScale(2, RoundingMode.HALF_UP);

            // 5. Taxes (Annual Vignette)
            BigDecimal annualVignette = (input.annualVignetteTax() != null)
                ? input.annualVignetteTax()
                : BigDecimal.ZERO;
            BigDecimal taxesCost = annualVignette
                .multiply(BigDecimal.valueOf(years))
                .setScale(2, RoundingMode.HALF_UP);

            // 6. Financing Opportunity Cost (assuming 70% financed @ market interest rate)
            BigDecimal financedPortion = price.multiply(BigDecimal.valueOf(0.70));
            BigDecimal financingCost = financedPortion
                .multiply(market.defaultFinancingInterestRatePct())
                .multiply(BigDecimal.valueOf(years))
                .multiply(BigDecimal.valueOf(0.55)) // Amortizing balance factor
                .setScale(2, RoundingMode.HALF_UP);

            // Total TCO
            BigDecimal totalTco = depreciation
                .add(energyCost)
                .add(maintenanceCost)
                .add(insuranceCost)
                .add(taxesCost)
                .add(financingCost)
                .setScale(2, RoundingMode.HALF_UP);

            int totalMonths = years * 12;
            BigDecimal monthlyAvg = totalTco
                .divide(BigDecimal.valueOf(totalMonths), 2, RoundingMode.HALF_UP);

            BigDecimal costPerKm = totalTco
                .divide(totalKm, 2, RoundingMode.HALF_UP);

            horizons.put(years, new TcoOutput.TcoHorizonBreakdown(
                years,
                totalTco,
                monthlyAvg,
                costPerKm,
                depreciation,
                energyCost,
                maintenanceCost,
                insuranceCost,
                taxesCost,
                financingCost,
                residualValue
            ));
        }

        // Generate Structured TCO Explanations for 5-Year Horizon
        TcoOutput.TcoHorizonBreakdown fiveYear = horizons.get(5);
        if ("BEV".equalsIgnoreCase(input.propulsionType())) {
            explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "TCO", "LOW_RUNNING_COSTS_EV",
                "Electric propulsion delivers substantially lower energy and routine maintenance expenditure over 5 years.", null, "TCO_CALCULATOR_V1"));
        }
        if (fiveYear.taxesCost().compareTo(BigDecimal.ZERO) == 0) {
            explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "TCO", "ZERO_ANNUAL_VIGNETTE",
                "Zero annual vignette tax liability in sovereign market (" + market.marketCode() + ").", null, "SOVEREIGN_TAX_RULE"));
        }

        String rawJson = serializeInput(input);
        return new TcoOutput(
            input.productId(),
            AlgorithmVersion.TCO_V1,
            market.currencyCode(),
            horizons,
            explanations,
            rawJson,
            SnapshotHasher.sha256Hex(rawJson)
        );
    }

    private String serializeInput(TcoInput input) {
        try {
            return objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            return "{\"productId\":" + input.productId() + "}";
        }
    }
}
