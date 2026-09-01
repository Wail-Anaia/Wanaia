package com.wanaia.domain.decision.calculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.model.*;
import com.wanaia.domain.decision.util.SnapshotHasher;
import com.wanaia.domain.mobility.dto.MobilityProductDetailDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

@Component
public class RecommendationEngineV1 {

    private final GlobalScoreCalculatorV1 globalScoreCalculator;
    private final PersonalFitScoreCalculatorV1 personalFitCalculator;
    private final TcoCalculatorV1 tcoCalculator;
    private final ExplanationEngineV1 explanationEngine;
    private final ObjectMapper objectMapper;

    public RecommendationEngineV1(
        GlobalScoreCalculatorV1 globalScoreCalculator,
        PersonalFitScoreCalculatorV1 personalFitCalculator,
        TcoCalculatorV1 tcoCalculator,
        ExplanationEngineV1 explanationEngine,
        ObjectMapper objectMapper
    ) {
        this.globalScoreCalculator = globalScoreCalculator;
        this.personalFitCalculator = personalFitCalculator;
        this.tcoCalculator = tcoCalculator;
        this.explanationEngine = explanationEngine;
        this.objectMapper = objectMapper;
    }

    public RecommendationOutput evaluateAndRank(RecommendationInput input) {
        if (input == null || input.userProfile() == null || input.candidateProducts() == null) {
            throw new IllegalArgumentException("RecommendationInput, userProfile, and candidateProducts must not be null");
        }

        MarketContext market = (input.marketContext() != null) ? input.marketContext() : MarketContext.morocco();
        List<RankedRecommendation> rankedList = new ArrayList<>();
        int totalEvaluated = input.candidateProducts().size();
        int qualifiedCount = 0;

        for (MobilityProductDetailDto product : input.candidateProducts()) {
            BigDecimal price = (product.safetyRatingNcap() != null)
                ? BigDecimal.valueOf(300000.00)
                : BigDecimal.valueOf(250000.00);

            Double consumption = (product.powertrain() != null && product.powertrain().wltpConsumptionMetric() != null)
                ? product.powertrain().wltpConsumptionMetric().doubleValue()
                : 6.0;

            Double accel = (product.powertrain() != null && product.powertrain().acceleration0100S() != null)
                ? product.powertrain().acceleration0100S().doubleValue()
                : 9.5;

            Double ncap = (product.safetyRatingNcap() != null) ? product.safetyRatingNcap().doubleValue() : null;

            // 1. Calculate Global Score
            GlobalScoreInput globalIn = new GlobalScoreInput(
                product.id(),
                product.variantName(),
                product.bodyStyle(),
                (product.powertrain() != null) ? product.powertrain().propulsionType() : "ICE",
                price,
                ncap,
                80.0,
                consumption,
                (product.powertrain() != null) ? product.powertrain().combinedPowerHp() : 120,
                accel,
                product.bootCapacityLiters(),
                product.seatCount(),
                0.65,
                Collections.emptyMap()
            );
            GlobalScoreOutput globalOut = globalScoreCalculator.calculate(globalIn);

            // 2. Calculate Personal Fit Score
            PersonalFitScoreInput fitIn = new PersonalFitScoreInput(
                product.id(),
                product.variantName(),
                product.bodyStyle(),
                (product.powertrain() != null) ? product.powertrain().propulsionType() : "ICE",
                price,
                consumption,
                product.seatCount(),
                2,
                product.bootCapacityLiters(),
                80.0,
                accel,
                input.userProfile()
            );
            PersonalFitScoreOutput fitOut = personalFitCalculator.calculate(fitIn);

            // 3. Calculate 5-Year TCO
            TcoInput tcoIn = new TcoInput(
                product.id(),
                price,
                (input.userProfile().getAnnualMileageKm() != null) ? input.userProfile().getAnnualMileageKm() : 15000,
                consumption,
                (product.powertrain() != null) ? product.powertrain().propulsionType() : "ICE",
                8,
                BigDecimal.valueOf(1500.00),
                market
            );
            TcoOutput tcoOut = tcoCalculator.calculate(tcoIn);
            TcoOutput.TcoHorizonBreakdown fiveYear = tcoOut.horizons().get(5);

            // 4. Blended Score: 70% Personal Fit + 30% Global Score
            BigDecimal blended = fitOut.fitScoreValue().multiply(BigDecimal.valueOf(0.70))
                .add(globalOut.scoreValue().multiply(BigDecimal.valueOf(0.30)))
                .setScale(2, RoundingMode.HALF_UP);

            if (fitOut.meetsHardConstraints()) {
                qualifiedCount++;
            }

            // Synthesize reasons
            List<ScoreExplanationItem> reasons = new ArrayList<>(fitOut.reasonsWhyFits());
            reasons.addAll(globalOut.explanations());

            List<ScoreExplanationItem> tradeOffs = new ArrayList<>(fitOut.reasonsWhyDoesNotFit());

            rankedList.add(new RankedRecommendation(
                product.id(),
                product.brandName(),
                product.modelName(),
                product.variantName(),
                0,
                blended,
                fitOut.fitScoreValue(),
                globalOut.scoreValue(),
                fiveYear.totalTco(),
                fiveYear.monthlyAverageCost(),
                fitOut.meetsHardConstraints(),
                fitOut.violatedHardConstraints(),
                reasons,
                tradeOffs
            ));
        }

        // Sort: Compliant first, then by blended score descending
        rankedList.sort((a, b) -> {
            if (a.meetsHardConstraints() != b.meetsHardConstraints()) {
                return a.meetsHardConstraints() ? -1 : 1;
            }
            return b.blendedScore().compareTo(a.blendedScore());
        });

        // Assign Ranks
        List<RankedRecommendation> finalRanked = new ArrayList<>();
        for (int i = 0; i < rankedList.size(); i++) {
            RankedRecommendation r = rankedList.get(i);
            finalRanked.add(new RankedRecommendation(
                r.productId(),
                r.brandName(),
                r.modelName(),
                r.variantName(),
                i + 1,
                r.blendedScore(),
                r.personalFitScore(),
                r.globalScore(),
                r.fiveYearTco(),
                r.monthlyTco(),
                r.meetsHardConstraints(),
                r.violatedConstraints(),
                r.reasonsWhyRecommended(),
                r.tradeOffs()
            ));
        }

        // Generate Comparison Rationale between Top 2
        String comparisonRationale = "No candidates available for comparison.";
        if (finalRanked.size() >= 2) {
            RankedRecommendation top = finalRanked.get(0);
            RankedRecommendation runnerUp = finalRanked.get(1);
            String advantage = (top.personalFitScore().compareTo(runnerUp.personalFitScore()) >= 0)
                ? "closer alignment with operational commute and passenger requirements"
                : "superior global reliability and lower 5-year operating expenditure";

            comparisonRationale = explanationEngine.generateComparisonRationale(
                top.brandName() + " " + top.modelName(),
                top.blendedScore().doubleValue(),
                runnerUp.brandName() + " " + runnerUp.modelName(),
                runnerUp.blendedScore().doubleValue(),
                advantage
            );
        } else if (finalRanked.size() == 1) {
            comparisonRationale = finalRanked.get(0).brandName() + " " + finalRanked.get(0).modelName() + " uniquely satisfies user operational profile.";
        }

        String rawJson;
        try {
            rawJson = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            rawJson = "{\"userId\":" + input.userId() + "}";
        }
        String snapshotHash = SnapshotHasher.sha256Hex(rawJson);

        return new RecommendationOutput(
            input.userId(),
            market.marketCode(),
            AlgorithmVersion.RECOMMENDATION_V1,
            totalEvaluated,
            qualifiedCount,
            finalRanked,
            comparisonRationale,
            Instant.now(),
            rawJson,
            snapshotHash
        );
    }
}
