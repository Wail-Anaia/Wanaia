# WANAIA — My Mobility Profile

## The Personalized Mobility Context Engine

---

## 1. Vision & Purpose

Traditional automotive websites treat every user identically.
WANAIA introduces **My Mobility Profile**: a structured, user-controlled representation of a person's physical, geographic, financial, familial, and operational mobility reality.

The Mobility Profile powers:
- **Personal WANAIA Fit Score ($S_{fit}$)**
- **Smart Natural Language Search & Discovery**
- **Personalized Recommendations & Alternatives**
- **Personalized Comparison Weightings**
- **Accurate Individualized TCO Calculations**
- **WANAIA AI Advisor Context Grounding**
- **Targeted Buyer Requests & Dealer Best Offers**

---

## 2. Mobility Profile Structure & Attributes

The Mobility Profile is modeled as a strongly typed domain entity with category-specific extension facets:

```java
public class MobilityProfile {
    // 1. Identity & Location Context
    private Long id;
    private Long userId;
    private String primaryCountryCode;      // e.g. "MA" (Morocco)
    private String primaryCity;             // e.g. "Casablanca"
    private String postalCode;
    private String preferredCurrency;       // "MAD", "EUR", "USD"

    // 2. Financial Boundaries & Purchasing Posture
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String purchaseTypePreference;  // "CASH", "FINANCING", "LEASING", "ANY"
    private BigDecimal maxMonthlyPayment;
    private String conditionPreference;     // "NEW_ONLY", "USED_ONLY", "CERTIFIED_OR_NEW", "ANY"
    private Integer ownershipHorizonYears;  // 1, 2, 3, 5, 10

    // 3. Operational & Usage Characteristics
    private Integer estimatedDailyMileageKm;
    private Integer estimatedAnnualMileageKm;
    private Integer cityDrivingPercentage;    // e.g. 70 (%)
    private Integer highwayDrivingPercentage; // e.g. 30 (%)
    private String primaryUsage;             // "DAILY_COMMUTE", "FAMILY_LONG_TRIPS", "RIDE_HAILING", "COMMERCIAL_DELIVERY"
    private Integer typicalPassengerCount;
    private Boolean isFamilyVehicle;
    private Integer isofixChildSeatsRequired;

    // 4. Infrastructure & Environmental Constraints
    private Boolean hasHomeChargingAccess;    // Wallbox / standard outlet at home
    private Boolean hasWorkplaceChargingAccess;
    private String parkingType;              // "PRIVATE_GARAGE", "STREET", "COVERED_PARKING"
    private Boolean narrowStreetConstraints; // Requires compact turning circle / width < 1850mm
    private Boolean roughRoadTerrainNeeds;   // Needs higher ground clearance (> 170mm) / AWD

    // 5. Priorities & Weight Preferences (1 to 5 scale or percentage weights)
    private Integer priorityReliability;     // 1 = Low, 5 = Critical
    private Integer priorityFuelEconomy;
    private Integer priorityComfortAndNoise;
    private Integer priorityPerformance;
    private Integer prioritySafetyAndAdas;
    private Integer priorityResaleValue;
    private Integer priorityTechnologyInfotainment;

    // 6. Mobility Category Specific Extensions (JSONB or Facet Entities)
    private CarProfileFacet carFacet;
    private MotorcycleProfileFacet motorcycleFacet;
    private CommercialProfileFacet commercialFacet;
}

public class CarProfileFacet {
    private List<String> preferredBodyTypes; // "SUV", "SEDAN", "HATCHBACK", "ESTATE"
    private List<String> preferredPowertrains; // "HYBRID", "PETROL", "DIESEL", "BEV", "PHEV"
    private Integer minimumBootSpaceLiters;
    private Boolean requiresAutomaticTransmission;
    private Boolean requiresSevenSeats;
    private Boolean requiresTrailerTowing;
}

public class MotorcycleProfileFacet {
    private Integer riderHeightCm;
    private Integer riderInseamCm;
    private String licenseCategory;          // "A1", "A2", "A", "B_PERMITTED"
    private String experienceLevel;          // "BEGINNER", "INTERMEDIATE", "VETERAN"
    private String ridingStyle;              // "COMMUTER", "WEEKEND_TOURING", "OFF_ROAD", "TRACK"
    private Boolean frequentPassengerRiding; // Pillion comfort priority
}

public class CommercialProfileFacet {
    private Integer minimumPayloadKg;
    private Double minimumCargoVolumeM3;
    private Boolean requiresRefrigeration;
    private Boolean requiresTailLift;
}
```

---

## 3. Database Schema

```sql
CREATE TABLE mobility_profiles (
    id                          BIGSERIAL PRIMARY KEY,
    user_id                     BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    
    -- Geographic & Financial
    country_code                VARCHAR(3) NOT NULL DEFAULT 'MA',
    city                        VARCHAR(100),
    preferred_currency          VARCHAR(3) NOT NULL DEFAULT 'MAD',
    budget_min                  DECIMAL(12,2),
    budget_max                  DECIMAL(12,2),
    max_monthly_payment         DECIMAL(12,2),
    condition_preference        VARCHAR(30) DEFAULT 'ANY',
    ownership_horizon_years     INTEGER DEFAULT 5,
    
    -- Operational
    daily_mileage_km            INTEGER DEFAULT 30,
    annual_mileage_km           INTEGER DEFAULT 12000,
    city_driving_pct            INTEGER DEFAULT 60,
    highway_driving_pct         INTEGER DEFAULT 40,
    primary_usage               VARCHAR(50) DEFAULT 'DAILY_COMMUTE',
    typical_passenger_count     INTEGER DEFAULT 2,
    isofix_seats_required       INTEGER DEFAULT 0,
    
    -- Infrastructure & Constraints
    has_home_charging           BOOLEAN DEFAULT FALSE,
    has_work_charging           BOOLEAN DEFAULT FALSE,
    parking_type                VARCHAR(30) DEFAULT 'STREET',
    rough_road_needs            BOOLEAN DEFAULT FALSE,
    
    -- Priority Weights (1 to 5)
    priority_reliability        INTEGER DEFAULT 4,
    priority_fuel_economy       INTEGER DEFAULT 4,
    priority_comfort            INTEGER DEFAULT 3,
    priority_performance        INTEGER DEFAULT 3,
    priority_safety             INTEGER DEFAULT 4,
    priority_resale             INTEGER DEFAULT 3,
    
    -- Category Facets
    car_facet                   JSONB,
    motorcycle_facet            JSONB,
    commercial_facet            JSONB,
    
    created_at                  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_mobility_profiles_user ON mobility_profiles(user_id);
```

---

## 4. Privacy & Consent Control

1. **User Ownership:** Users can view, edit, export, or reset their Mobility Profile at any time.
2. **Anonymous / Guest Support:** Guest users can create an in-memory/session-based Mobility Profile without registration to experience the Personal Fit Score and TCO calculation instantly.
3. **Data Minimization:** No unnecessary PII (such as exact home addresses) is stored in the profile; only postal code / city level geography is retained for infrastructure and tax resolution.

---

*The Mobility Profile is the foundational bridge between objective vehicle knowledge and subjective human decision-making.*
