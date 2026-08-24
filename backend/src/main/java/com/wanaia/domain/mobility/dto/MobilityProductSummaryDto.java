package com.wanaia.domain.mobility.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MobilityProductSummaryDto(
    Long id,
    UUID uuid,
    String brandName,
    String modelName,
    String generationName,
    String variantName,
    String slug,
    String bodyStyle,
    Integer seatCount,
    Integer combinedPowerHp,
    String propulsionType,
    String primaryFuel,
    BigDecimal wltpConsumptionMetric,
    BigDecimal safetyRatingNcap
) {}
