package com.wanaia.domain.decision.model;

import java.math.BigDecimal;

public record DealScoreInput(
    String listingId,
    Long productId,
    BigDecimal askingPrice,
    BigDecimal baseMarketEstimate,
    int marketSampleSize,
    int mileageKm,
    int benchmarkMileageKm,
    int vehicleAgeYears,
    String conditionGrade, // "EXCELLENT", "GOOD", "FAIR"
    String currencyCode
) {}
