package com.wanaia.domain.decision.model;

import java.math.BigDecimal;
import java.util.List;

public record DealScoreOutput(
    String listingId,
    Long productId,
    String algorithmVersion,
    DealRating rating,
    BigDecimal dealScoreValue,           // 0.00 - 100.00 (or null if INSUFFICIENT_DATA)
    BigDecimal askingPrice,
    BigDecimal adjustedMarketValue,      // Expected market value after mileage & age adjustments
    BigDecimal priceDifferenceAmount,    // askingPrice - adjustedMarketValue
    BigDecimal priceDifferencePercentage,// (askingPrice - adjustedMarketValue) / adjustedMarketValue * 100
    BigDecimal confidenceLevel,          // 0.00 - 1.00
    List<ScoreExplanationItem> explanations,
    String rawSnapshotJson,
    String snapshotHash
) {}
