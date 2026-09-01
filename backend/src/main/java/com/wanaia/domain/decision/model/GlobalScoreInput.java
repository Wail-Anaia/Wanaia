package com.wanaia.domain.decision.model;

import java.math.BigDecimal;
import java.util.Map;

public record GlobalScoreInput(
    Long productId,
    String variantName,
    String bodyStyle,
    String propulsionType,
    BigDecimal msrpPrice,
    Double ncapStars,            // 0.0 - 5.0
    Double reliabilityScore,     // 0.0 - 100.0 (from historical reliability index / warranty data)
    Double fuelConsumptionMetric,// L/100km or kWh/100km
    Integer powerHp,
    Double acceleration0100s,
    Integer bootCapacityLiters,
    Integer seatCount,
    Double historicalResaleRetentionPct, // e.g. 0.65 after 3 years
    Map<String, Double> attributeConfidenceMap // e.g. {"reliability": 0.90, "safety": 1.00}
) {}
