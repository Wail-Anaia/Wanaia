package com.wanaia.domain.profile.dto;

import java.math.BigDecimal;

public record MobilityProfileDto(
    Long id,
    Long userId,
    String countryCode,
    String city,
    String preferredCurrency,
    BigDecimal budgetMin,
    BigDecimal budgetMax,
    BigDecimal maxMonthlyPayment,
    String conditionPreference,
    Integer ownershipHorizonYears,
    Integer dailyMileageKm,
    Integer annualMileageKm,
    Integer cityDrivingPct,
    Integer highwayDrivingPct,
    String primaryUsage,
    Integer typicalPassengerCount,
    Integer isofixSeatsRequired,
    Boolean hasHomeCharging,
    Boolean hasWorkCharging,
    String parkingType,
    Boolean roughRoadNeeds,
    Integer priorityReliability,
    Integer priorityFuelEconomy,
    Integer priorityComfort,
    Integer priorityPerformance,
    Integer prioritySafety,
    Integer priorityResale
) {}
