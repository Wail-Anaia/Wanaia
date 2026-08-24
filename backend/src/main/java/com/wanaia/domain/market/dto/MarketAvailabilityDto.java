package com.wanaia.domain.market.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MarketAvailabilityDto(
    Long id,
    Long productId,
    Long marketId,
    String marketCode,
    Long modelYearId,
    Integer modelYear,
    String localTrimName,
    BigDecimal msrpBasePrice,
    String currencyCode,
    Integer fiscalHorsepowerCv,
    BigDecimal annualVignetteTax,
    Integer warrantyYears,
    Integer warrantyKm,
    Boolean isOrderable,
    LocalDate effectiveDate
) {}
