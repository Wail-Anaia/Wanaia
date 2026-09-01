package com.wanaia.domain.decision.model;

import com.wanaia.domain.profile.model.MobilityProfile;

import java.math.BigDecimal;

public record PersonalFitScoreInput(
    Long productId,
    String variantName,
    String bodyStyle,
    String propulsionType,
    BigDecimal localPrice,
    Double fuelConsumptionMetric,
    Integer seatCount,
    Integer isofixCount,
    Integer bootCapacityLiters,
    Double reliabilityScore,
    Double acceleration0100s,
    MobilityProfile userProfile
) {}
