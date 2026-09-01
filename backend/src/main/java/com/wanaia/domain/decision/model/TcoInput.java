package com.wanaia.domain.decision.model;

import java.math.BigDecimal;

public record TcoInput(
    Long productId,
    BigDecimal purchasePrice,
    int annualMileageKm,
    Double fuelConsumptionMetric,
    String propulsionType,
    Integer fiscalHpCV,
    BigDecimal annualVignetteTax,
    MarketContext marketContext
) {}
