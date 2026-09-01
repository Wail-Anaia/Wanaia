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
public class GlobalScoreCalculatorV1 {

    private final ObjectMapper objectMapper;

    // Dimension Weights (Sum = 1.00)
    public static final BigDecimal WEIGHT_RELIABILITY  = BigDecimal.valueOf(0.20);
    public static final BigDecimal WEIGHT_SAFETY       = BigDecimal.valueOf(0.15);
    public static final BigDecimal WEIGHT_EFFICIENCY   = BigDecimal.valueOf(0.15);
    public static final BigDecimal WEIGHT_VALUE        = BigDecimal.valueOf(0.15);
    public static final BigDecimal WEIGHT_PRACTICALITY = BigDecimal.valueOf(0.15);
    public static final BigDecimal WEIGHT_PERFORMANCE  = BigDecimal.valueOf(0.10);
    public static final BigDecimal WEIGHT_RESALE       = BigDecimal.valueOf(0.10);

    public GlobalScoreCalculatorV1(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GlobalScoreOutput calculate(GlobalScoreInput input) {
        if (input == null) {
            throw new IllegalArgumentException("GlobalScoreInput must not be null");
        }

        List<ScoreExplanationItem> explanations = new ArrayList<>();
        Map<String, BigDecimal> dimensionScores = new LinkedHashMap<>();

        // 1. Reliability (20%) - Default: 75.0 if unmeasured
        double relVal = (input.reliabilityScore() != null) ? input.reliabilityScore() : 75.0;
        relVal = Math.max(0.0, Math.min(100.0, relVal));
        BigDecimal scoreReliability = BigDecimal.valueOf(relVal).setScale(2, RoundingMode.HALF_UP);
        dimensionScores.put("reliability", scoreReliability);

        if (relVal >= 85.0) {
            explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "RELIABILITY", "HIGH_RELIABILITY_INDEX",
                "Exceptional powertrain and mechanical reliability index rating (" + relVal + "/100).", null, "HISTORICAL_RELIABILITY"));
        } else if (relVal < 60.0) {
            explanations.add(new ScoreExplanationItem(null, ExplanationType.CON, "RELIABILITY", "BELOW_AVERAGE_RELIABILITY",
                "Higher than average reported frequency of maintenance interventions.", null, "HISTORICAL_RELIABILITY"));
        }

        // 2. Safety (15%) - Euro NCAP (5 stars = 100, 4 stars = 80, 3 stars = 60, unrated = 70)
        double safetyVal = 70.0;
        if (input.ncapStars() != null) {
            safetyVal = Math.max(0.0, Math.min(5.0, input.ncapStars())) * 20.0;
            if (input.ncapStars() >= 5.0) {
                explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "SAFETY", "FIVE_STAR_NCAP_SAFETY",
                    "Top 5-star crash safety and occupant protection certification.", null, "EURO_NCAP"));
            }
        }
        BigDecimal scoreSafety = BigDecimal.valueOf(safetyVal).setScale(2, RoundingMode.HALF_UP);
        dimensionScores.put("safety", scoreSafety);

        // 3. Efficiency (15%) - Benchmark: 4.0 L/100km or 14 kWh/100km -> 100, 10.0 L/100km -> 40
        double effVal = 70.0;
        if (input.fuelConsumptionMetric() != null && input.fuelConsumptionMetric() > 0) {
            double c = input.fuelConsumptionMetric();
            if ("BEV".equalsIgnoreCase(input.propulsionType())) {
                effVal = Math.max(30.0, Math.min(100.0, 100.0 - (c - 14.0) * 4.0));
            } else {
                effVal = Math.max(20.0, Math.min(100.0, 100.0 - (c - 4.0) * 10.0));
            }
            if (effVal >= 85.0) {
                explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "EFFICIENCY", "SUPERIOR_ENERGY_EFFICIENCY",
                    "Exceptional fuel economy / energy consumption (" + c + " standard units).", null, "WLTP_HOMOLOGATION"));
            }
        }
        BigDecimal scoreEfficiency = BigDecimal.valueOf(effVal).setScale(2, RoundingMode.HALF_UP);
        dimensionScores.put("efficiency", scoreEfficiency);

        // 4. Value (15%) - Quality to Price Ratio
        double valueVal = 75.0;
        BigDecimal scoreValue = BigDecimal.valueOf(valueVal).setScale(2, RoundingMode.HALF_UP);
        dimensionScores.put("value", scoreValue);

        // 5. Practicality (15%) - Boot capacity & seat count
        double practVal = 70.0;
        int boot = (input.bootCapacityLiters() != null) ? input.bootCapacityLiters() : 400;
        if (boot >= 550) {
            practVal = 95.0;
            explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "PRACTICALITY", "EXPANSIVE_CARGO_VOLUME",
                "Class-leading cargo boot capacity of " + boot + " liters.", null, "MANUFACTURER_SPECS"));
        } else if (boot >= 400) {
            practVal = 80.0;
        } else if (boot >= 250) {
            practVal = 65.0;
        } else {
            practVal = 50.0;
        }
        BigDecimal scorePracticality = BigDecimal.valueOf(practVal).setScale(2, RoundingMode.HALF_UP);
        dimensionScores.put("practicality", scorePracticality);

        // 6. Performance (10%) - 0-100s & Power
        double perfVal = 70.0;
        if (input.acceleration0100s() != null && input.acceleration0100s() > 0) {
            double accel = input.acceleration0100s();
            perfVal = Math.max(30.0, Math.min(100.0, 100.0 - (accel - 4.0) * 6.5));
            if (accel <= 6.0) {
                explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "PERFORMANCE", "DYNAMIC_ACCELERATION",
                    "Rapid 0-100 km/h acceleration in " + accel + " seconds.", null, "MANUFACTURER_SPECS"));
            }
        }
        BigDecimal scorePerformance = BigDecimal.valueOf(perfVal).setScale(2, RoundingMode.HALF_UP);
        dimensionScores.put("performance", scorePerformance);

        // 7. Resale Retention (10%)
        double resaleVal = (input.historicalResaleRetentionPct() != null)
            ? input.historicalResaleRetentionPct() * 100.0
            : 65.0;
        BigDecimal scoreResale = BigDecimal.valueOf(resaleVal).setScale(2, RoundingMode.HALF_UP);
        dimensionScores.put("resale", scoreResale);

        // Compute Weighted Global Score
        BigDecimal weightedSum = scoreReliability.multiply(WEIGHT_RELIABILITY)
            .add(scoreSafety.multiply(WEIGHT_SAFETY))
            .add(scoreEfficiency.multiply(WEIGHT_EFFICIENCY))
            .add(scoreValue.multiply(WEIGHT_VALUE))
            .add(scorePracticality.multiply(WEIGHT_PRACTICALITY))
            .add(scorePerformance.multiply(WEIGHT_PERFORMANCE))
            .add(scoreResale.multiply(WEIGHT_RESALE))
            .setScale(2, RoundingMode.HALF_UP);

        // Rating Class
        String ratingClass;
        double globalDbl = weightedSum.doubleValue();
        if (globalDbl >= 85.0) {
            ratingClass = "EXCELLENT";
        } else if (globalDbl >= 70.0) {
            ratingClass = "GOOD";
        } else if (globalDbl >= 55.0) {
            ratingClass = "FAIR";
        } else {
            ratingClass = "POOR";
        }

        // Confidence Calculation (derived from attribute presence)
        double confidence = 0.70;
        if (input.reliabilityScore() != null) confidence += 0.10;
        if (input.ncapStars() != null) confidence += 0.10;
        if (input.fuelConsumptionMetric() != null) confidence += 0.05;
        if (input.acceleration0100s() != null) confidence += 0.05;
        confidence = Math.min(1.00, confidence);
        BigDecimal confLevel = BigDecimal.valueOf(confidence).setScale(2, RoundingMode.HALF_UP);

        // Snapshot Hash
        String rawJson;
        try {
            rawJson = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            rawJson = "{\"productId\":" + input.productId() + "}";
        }
        String snapshotHash = SnapshotHasher.sha256Hex(rawJson);

        return new GlobalScoreOutput(
            input.productId(),
            AlgorithmVersion.GLOBAL_SCORE_V1,
            weightedSum,
            ratingClass,
            confLevel,
            dimensionScores,
            explanations,
            rawJson,
            snapshotHash
        );
    }
}
