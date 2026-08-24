package com.wanaia.domain.mobility.dto;

import java.math.BigDecimal;

public record PowertrainConfigurationDto(
    Long id,
    String code,
    String propulsionType,
    String primaryFuel,
    String driveLayout,
    Integer combinedPowerHp,
    Integer combinedPowerKw,
    Integer combinedTorqueNm,
    BigDecimal acceleration0100S,
    Integer topSpeedKmh,
    BigDecimal wltpConsumptionMetric,
    Integer wltpCo2Gkm,
    Integer evRangeWltpKm
) {}
