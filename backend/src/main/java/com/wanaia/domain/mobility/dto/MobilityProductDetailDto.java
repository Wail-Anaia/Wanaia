package com.wanaia.domain.mobility.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MobilityProductDetailDto(
    Long id,
    UUID uuid,
    Long generationId,
    String brandName,
    String modelName,
    String generationName,
    String variantName,
    String slug,
    String bodyStyle,
    Integer seatCount,
    Integer curbWeightKg,
    Integer lengthMm,
    Integer widthMm,
    Integer heightMm,
    Integer wheelbaseMm,
    Integer bootCapacityLiters,
    BigDecimal safetyRatingNcap,
    PowertrainConfigurationDto powertrain
) {}
