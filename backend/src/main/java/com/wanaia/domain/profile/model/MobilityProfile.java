package com.wanaia.domain.profile.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "mobility_profiles", indexes = {
    @Index(name = "idx_mobility_profiles_user", columnList = "user_id")
})
public class MobilityProfile extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // Referenced by ID (Aggregate Boundary)

    // Geographic & Currency Context
    @Column(name = "country_code", nullable = false, length = 3)
    private String countryCode = "MA";

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "preferred_currency", nullable = false, length = 3)
    private String preferredCurrency = "MAD";

    // Financial Boundaries
    @Column(name = "budget_min", precision = 12, scale = 2)
    private BigDecimal budgetMin;

    @Column(name = "budget_max", precision = 12, scale = 2)
    private BigDecimal budgetMax;

    @Column(name = "max_monthly_payment", precision = 12, scale = 2)
    private BigDecimal maxMonthlyPayment;

    @Column(name = "condition_preference", length = 30)
    private String conditionPreference = "ANY"; // "NEW_ONLY", "USED_ONLY", "CERTIFIED_OR_NEW", "ANY"

    @Column(name = "ownership_horizon_years")
    private Integer ownershipHorizonYears = 5;

    // Operational Mobility Characteristics
    @Column(name = "daily_mileage_km")
    private Integer dailyMileageKm = 30;

    @Column(name = "annual_mileage_km")
    private Integer annualMileageKm = 12000;

    @Column(name = "city_driving_pct")
    private Integer cityDrivingPct = 60;

    @Column(name = "highway_driving_pct")
    private Integer highwayDrivingPct = 40;

    @Column(name = "primary_usage", length = 50)
    private String primaryUsage = "DAILY_COMMUTE";

    @Column(name = "typical_passenger_count")
    private Integer typicalPassengerCount = 2;

    @Column(name = "isofix_seats_required")
    private Integer isofixSeatsRequired = 0;

    // Infrastructure & Environmental Constraints
    @Column(name = "has_home_charging")
    private Boolean hasHomeCharging = false;

    @Column(name = "has_work_charging")
    private Boolean hasWorkCharging = false;

    @Column(name = "parking_type", length = 30)
    private String parkingType = "STREET";

    @Column(name = "rough_road_needs")
    private Boolean roughRoadNeeds = false;

    // Relative Priorities (1-5 scale)
    @Column(name = "priority_reliability")
    private Integer priorityReliability = 4;

    @Column(name = "priority_fuel_economy")
    private Integer priorityFuelEconomy = 4;

    @Column(name = "priority_comfort")
    private Integer priorityComfort = 3;

    @Column(name = "priority_performance")
    private Integer priorityPerformance = 3;

    @Column(name = "priority_safety")
    private Integer prioritySafety = 4;

    @Column(name = "priority_resale")
    private Integer priorityResale = 3;

    // Category Specific Extension Facets (JSON string representation)
    @Column(name = "car_facet_json", columnDefinition = "TEXT")
    private String carFacetJson;

    @Column(name = "motorcycle_facet_json", columnDefinition = "TEXT")
    private String motorcycleFacetJson;

    @Column(name = "commercial_facet_json", columnDefinition = "TEXT")
    private String commercialFacetJson;

    public MobilityProfile() {}

    public MobilityProfile(Long userId, String countryCode, String preferredCurrency, BigDecimal budgetMax) {
        this.userId = userId;
        this.countryCode = countryCode;
        this.preferredCurrency = preferredCurrency;
        this.budgetMax = budgetMax;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public BigDecimal getBudgetMin() {
        return budgetMin;
    }

    public void setBudgetMin(BigDecimal budgetMin) {
        this.budgetMin = budgetMin;
    }

    public BigDecimal getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(BigDecimal budgetMax) {
        this.budgetMax = budgetMax;
    }

    public BigDecimal getMaxMonthlyPayment() {
        return maxMonthlyPayment;
    }

    public void setMaxMonthlyPayment(BigDecimal maxMonthlyPayment) {
        this.maxMonthlyPayment = maxMonthlyPayment;
    }

    public String getConditionPreference() {
        return conditionPreference;
    }

    public void setConditionPreference(String conditionPreference) {
        this.conditionPreference = conditionPreference;
    }

    public Integer getOwnershipHorizonYears() {
        return ownershipHorizonYears;
    }

    public void setOwnershipHorizonYears(Integer ownershipHorizonYears) {
        this.ownershipHorizonYears = ownershipHorizonYears;
    }

    public Integer getDailyMileageKm() {
        return dailyMileageKm;
    }

    public void setDailyMileageKm(Integer dailyMileageKm) {
        this.dailyMileageKm = dailyMileageKm;
    }

    public Integer getAnnualMileageKm() {
        return annualMileageKm;
    }

    public void setAnnualMileageKm(Integer annualMileageKm) {
        this.annualMileageKm = annualMileageKm;
    }

    public Integer getCityDrivingPct() {
        return cityDrivingPct;
    }

    public void setCityDrivingPct(Integer cityDrivingPct) {
        this.cityDrivingPct = cityDrivingPct;
    }

    public Integer getHighwayDrivingPct() {
        return highwayDrivingPct;
    }

    public void setHighwayDrivingPct(Integer highwayDrivingPct) {
        this.highwayDrivingPct = highwayDrivingPct;
    }

    public String getPrimaryUsage() {
        return primaryUsage;
    }

    public void setPrimaryUsage(String primaryUsage) {
        this.primaryUsage = primaryUsage;
    }

    public Integer getTypicalPassengerCount() {
        return typicalPassengerCount;
    }

    public void setTypicalPassengerCount(Integer typicalPassengerCount) {
        this.typicalPassengerCount = typicalPassengerCount;
    }

    public Integer getIsofixSeatsRequired() {
        return isofixSeatsRequired;
    }

    public void setIsofixSeatsRequired(Integer isofixSeatsRequired) {
        this.isofixSeatsRequired = isofixSeatsRequired;
    }

    public Boolean getHasHomeCharging() {
        return hasHomeCharging;
    }

    public void setHasHomeCharging(Boolean hasHomeCharging) {
        this.hasHomeCharging = hasHomeCharging;
    }

    public Boolean getHasWorkCharging() {
        return hasWorkCharging;
    }

    public void setHasWorkCharging(Boolean hasWorkCharging) {
        this.hasWorkCharging = hasWorkCharging;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public Boolean getRoughRoadNeeds() {
        return roughRoadNeeds;
    }

    public void setRoughRoadNeeds(Boolean roughRoadNeeds) {
        this.roughRoadNeeds = roughRoadNeeds;
    }

    public Integer getPriorityReliability() {
        return priorityReliability;
    }

    public void setPriorityReliability(Integer priorityReliability) {
        this.priorityReliability = priorityReliability;
    }

    public Integer getPriorityFuelEconomy() {
        return priorityFuelEconomy;
    }

    public void setPriorityFuelEconomy(Integer priorityFuelEconomy) {
        this.priorityFuelEconomy = priorityFuelEconomy;
    }

    public Integer getPriorityComfort() {
        return priorityComfort;
    }

    public void setPriorityComfort(Integer priorityComfort) {
        this.priorityComfort = priorityComfort;
    }

    public Integer getPriorityPerformance() {
        return priorityPerformance;
    }

    public void setPriorityPerformance(Integer priorityPerformance) {
        this.priorityPerformance = priorityPerformance;
    }

    public Integer getPrioritySafety() {
        return prioritySafety;
    }

    public void setPrioritySafety(Integer prioritySafety) {
        this.prioritySafety = prioritySafety;
    }

    public Integer getPriorityResale() {
        return priorityResale;
    }

    public void setPriorityResale(Integer priorityResale) {
        this.priorityResale = priorityResale;
    }

    public String getCarFacetJson() {
        return carFacetJson;
    }

    public void setCarFacetJson(String carFacetJson) {
        this.carFacetJson = carFacetJson;
    }

    public String getMotorcycleFacetJson() {
        return motorcycleFacetJson;
    }

    public void setMotorcycleFacetJson(String motorcycleFacetJson) {
        this.motorcycleFacetJson = motorcycleFacetJson;
    }

    public String getCommercialFacetJson() {
        return commercialFacetJson;
    }

    public void setCommercialFacetJson(String commercialFacetJson) {
        this.commercialFacetJson = commercialFacetJson;
    }
}
