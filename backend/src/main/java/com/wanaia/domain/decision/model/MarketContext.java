package com.wanaia.domain.decision.model;

import java.math.BigDecimal;

public record MarketContext(
    String marketCode,          // "MAR", "FRA", "ARE"
    String currencyCode,        // "MAD", "EUR", "AED"
    BigDecimal fuelPricePetrolPerLiter,
    BigDecimal fuelPriceDieselPerLiter,
    BigDecimal electricityPricePerKwh,
    BigDecimal defaultAnnualInsuranceRatePct, // e.g. 0.035 (3.5% of vehicle value)
    BigDecimal defaultFinancingInterestRatePct, // e.g. 0.065 (6.5% APR)
    BigDecimal baseLaborRatePerHour
) {
    public static MarketContext morocco() {
        return new MarketContext(
            "MAR",
            "MAD",
            BigDecimal.valueOf(14.50), // 14.50 MAD/L petrol
            BigDecimal.valueOf(12.80), // 12.80 MAD/L diesel
            BigDecimal.valueOf(1.60),  // 1.60 MAD/kWh electricity
            BigDecimal.valueOf(0.040), // 4.0% comprehensive insurance
            BigDecimal.valueOf(0.070), // 7.0% APR
            BigDecimal.valueOf(250.00) // 250 MAD/hr standard workshop
        );
    }

    public static MarketContext france() {
        return new MarketContext(
            "FRA",
            "EUR",
            BigDecimal.valueOf(1.85),  // 1.85 EUR/L petrol
            BigDecimal.valueOf(1.75),  // 1.75 EUR/L diesel
            BigDecimal.valueOf(0.25),  // 0.25 EUR/kWh electricity
            BigDecimal.valueOf(0.030), // 3.0% comprehensive insurance
            BigDecimal.valueOf(0.045), // 4.5% APR
            BigDecimal.valueOf(85.00)  // 85 EUR/hr standard workshop
        );
    }
}
