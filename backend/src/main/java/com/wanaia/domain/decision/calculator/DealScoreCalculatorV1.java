package com.wanaia.domain.decision.calculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.model.*;
import com.wanaia.domain.decision.util.SnapshotHasher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class DealScoreCalculatorV1 {

    private final ObjectMapper objectMapper;
    public static final int MIN_SAMPLE_SIZE_THRESHOLD = 5;

    public DealScoreCalculatorV1(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DealScoreOutput calculate(DealScoreInput input) {
        if (input == null || input.askingPrice() == null) {
            throw new IllegalArgumentException("DealScoreInput and askingPrice must not be null");
        }

        List<ScoreExplanationItem> explanations = new ArrayList<>();

        // 1. Check Sample Size Threshold
        if (input.marketSampleSize() < MIN_SAMPLE_SIZE_THRESHOLD || input.baseMarketEstimate() == null) {
            explanations.add(new ScoreExplanationItem(null, ExplanationType.WARNING, "MARKET_VALUATION", "INSUFFICIENT_MARKET_DATA",
                "Insufficient comparable market listings in active market sample (" + input.marketSampleSize() + " available, minimum " + MIN_SAMPLE_SIZE_THRESHOLD + " required).", null, "MARKETPLACE_OBSERVATION"));

            String rawJson = serializeInput(input);
            return new DealScoreOutput(
                input.listingId(),
                input.productId(),
                AlgorithmVersion.DEAL_SCORE_V1,
                DealRating.INSUFFICIENT_DATA,
                null,
                input.askingPrice(),
                null,
                null,
                null,
                BigDecimal.valueOf(0.30),
                explanations,
                rawJson,
                SnapshotHasher.sha256Hex(rawJson)
            );
        }

        // 2. Mileage Adjustment (e.g. 0.05 currency units per km delta)
        int mileageDeltaKm = input.benchmarkMileageKm() - input.mileageKm();
        BigDecimal mileageAdjustment = BigDecimal.valueOf(mileageDeltaKm)
            .multiply(BigDecimal.valueOf(0.05))
            .setScale(2, RoundingMode.HALF_UP);

        // 3. Condition Adjustment
        BigDecimal conditionMultiplier = BigDecimal.ZERO;
        if ("EXCELLENT".equalsIgnoreCase(input.conditionGrade())) {
            conditionMultiplier = BigDecimal.valueOf(0.04);
        } else if ("FAIR".equalsIgnoreCase(input.conditionGrade())) {
            conditionMultiplier = BigDecimal.valueOf(-0.06);
        }
        BigDecimal conditionAdjustment = input.baseMarketEstimate()
            .multiply(conditionMultiplier)
            .setScale(2, RoundingMode.HALF_UP);

        // 4. Adjusted Market Value
        BigDecimal adjustedMarketValue = input.baseMarketEstimate()
            .add(mileageAdjustment)
            .add(conditionAdjustment)
            .setScale(2, RoundingMode.HALF_UP);

        if (adjustedMarketValue.compareTo(BigDecimal.ZERO) <= 0) {
            adjustedMarketValue = input.baseMarketEstimate();
        }

        // 5. Price Difference Amount & Percentage
        BigDecimal priceDiff = input.askingPrice().subtract(adjustedMarketValue).setScale(2, RoundingMode.HALF_UP);
        BigDecimal priceDiffPct = priceDiff
            .divide(adjustedMarketValue, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);

        // 6. Classification & Score
        DealRating rating;
        BigDecimal scoreValue;
        double diffPct = priceDiffPct.doubleValue();

        if (diffPct < -8.0) {
            rating = DealRating.EXCELLENT_DEAL;
            scoreValue = BigDecimal.valueOf(95.00);
            explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "PRICING", "EXCELLENT_DEAL_BELOW_MARKET",
                "Priced " + priceDiffPct.abs() + "% below adjusted fair market value.", null, "MARKET_VALUATION_ENGINE"));
        } else if (diffPct < -3.0) {
            rating = DealRating.GOOD_DEAL;
            scoreValue = BigDecimal.valueOf(85.00);
            explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "PRICING", "GOOD_DEAL_BELOW_MARKET",
                "Priced " + priceDiffPct.abs() + "% below adjusted fair market value.", null, "MARKET_VALUATION_ENGINE"));
        } else if (diffPct <= 3.0) {
            rating = DealRating.FAIR_PRICE;
            scoreValue = BigDecimal.valueOf(70.00);
            explanations.add(new ScoreExplanationItem(null, ExplanationType.PRO, "PRICING", "FAIR_MARKET_PRICE",
                "Price is consistent with current sovereign market trading benchmarks.", null, "MARKET_VALUATION_ENGINE"));
        } else if (diffPct <= 10.0) {
            rating = DealRating.EXPENSIVE;
            scoreValue = BigDecimal.valueOf(45.00);
            explanations.add(new ScoreExplanationItem(null, ExplanationType.CON, "PRICING", "ABOVE_MARKET_PRICE",
                "Priced " + priceDiffPct + "% above prevailing market comparable valuations.", null, "MARKET_VALUATION_ENGINE"));
        } else {
            rating = DealRating.VERY_EXPENSIVE;
            scoreValue = BigDecimal.valueOf(20.00);
            explanations.add(new ScoreExplanationItem(null, ExplanationType.WARNING, "PRICING", "SUBSTANTIALLY_OVERPRICED",
                "Priced significantly above fair market valuation (" + priceDiffPct + "% premium).", null, "MARKET_VALUATION_ENGINE"));
        }

        // Confidence (sample size scaling)
        double confidence = Math.min(0.95, 0.60 + (input.marketSampleSize() * 0.02));
        BigDecimal confLevel = BigDecimal.valueOf(confidence).setScale(2, RoundingMode.HALF_UP);

        String rawJson = serializeInput(input);
        return new DealScoreOutput(
            input.listingId(),
            input.productId(),
            AlgorithmVersion.DEAL_SCORE_V1,
            rating,
            scoreValue,
            input.askingPrice(),
            adjustedMarketValue,
            priceDiff,
            priceDiffPct,
            confLevel,
            explanations,
            rawJson,
            SnapshotHasher.sha256Hex(rawJson)
        );
    }

    private String serializeInput(DealScoreInput input) {
        try {
            return objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            return "{\"listingId\":\"" + input.listingId() + "\"}";
        }
    }
}
