# WANAIA — Database Architecture & Conceptual Entity Model

## Relational Schema, Entities, and Cardinalities

---

## 1. Complete Conceptual Entity Model & Cardinalities

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           ENTITY RELATIONSHIP MAP                           │
└─────────────────────────────────────────────────────────────────────────────┘

 MobilityCategory (1) ───< (N) Brand
 Brand (1) ──────────────< (N) Model
 Model (1) ──────────────< (N) Generation
 Generation (1) ─────────< (N) MobilityProduct
 MobilityProduct (N) ─── (1) PowertrainConfiguration
 
 PowertrainConfiguration (1) ───< (N) EngineMapping ───> (1) Engine
 PowertrainConfiguration (1) ───< (N) MotorMapping ────> (1) ElectricMotor
 PowertrainConfiguration (1) ───o (1) Battery
 PowertrainConfiguration (1) ───o (1) Transmission
 
 MobilityProduct (1) ────< (N) MarketAvailability
 Market (1) ─────────────< (N) MarketAvailability
 ModelYear (1) ──────────< (N) MarketAvailability
 
 MarketAvailability (1) ─o (N) MarketplaceListing
 User (1) ───────────────< (N) MarketplaceListing
 Dealer (1) ─────────────< (N) MarketplaceListing
 MarketplaceListing (1) ─< (N) ListingImage
 
 User (1) ───────────────o (1) MobilityProfile
 User (1) ───────────────< (N) GarageVehicle
 MobilityProduct (1) ────< (N) Review
 User (1) ───────────────< (N) Review
 
 ScoreResult (1) ───────── (1) ScoreInputSnapshot
 ScoreResult (1) ────────< (N) ScoreExplanationItem
 RecommendationTrace (N) ─> (1) MobilityProfile
 
 DataSource (1) ─────────< (N) AttributeProvenance
```

---

## 2. Entity Specifications & Field Dictionary

### 2.1 Taxonomy & Mobility Catalog Aggregate
1. **`MobilityCategory`**: `id` (PK), `code` (Unique: `CAR`, `MOTORCYCLE`, `SCOOTER`, `COMMERCIAL_VAN`, `TRUCK`, `E_BIKE`), `name_translations` (JSONB), `icon_url`, `display_order`, `is_active`.
2. **`Brand`**: `id` (PK), `category_id` (FK), `name`, `slug` (Unique per category), `logo_url`, `country_of_origin`, `founded_year`, `is_active`. *(Cardinality: Category 1:N Brands)*.
3. **`Model`**: `id` (PK), `brand_id` (FK), `name`, `slug` (Unique per brand), `segment_code` (e.g. `B_SUV`, `COMPACT`), `is_active`. *(Cardinality: Brand 1:N Models)*.
4. **`Generation`**: `id` (PK), `model_id` (FK), `name`, `slug`, `internal_platform_code` (e.g. `XA50`), `start_year`, `end_year`, `hero_image_url`, `is_current`. *(Cardinality: Model 1:N Generations)*.
5. **`MobilityProduct`**: `id` (PK), `generation_id` (FK), `variant_name`, `slug`, `body_style`, `powertrain_config_id` (FK), `curb_weight_kg`, `wheelbase_mm`, `length_mm`, `width_mm`, `height_mm`, `boot_capacity_liters`, `safety_rating_ncap`, `is_active`. *(Cardinality: Generation 1:N Products)*.

### 2.2 Powertrain & Energy System Aggregate
6. **`PowertrainConfiguration`**: `id` (PK), `code`, `propulsion_type` (`ICE`, `MHEV`, `HEV`, `PHEV`, `BEV`, `FCEV`), `transmission_id` (FK), `battery_id` (FK, Nullable), `drive_layout` (`FWD`, `RWD`, `AWD`, `4WD`), `combined_power_hp`, `combined_power_kw`, `combined_torque_nm`, `acceleration_0_100_s`, `top_speed_kmh`, `wltp_consumption_metric`, `wltp_co2_gkm`.
7. **`Engine`**: `id` (PK), `engine_code`, `fuel_type` (`PETROL`, `DIESEL`), `displacement_cc`, `cylinders`, `valves_per_cylinder`, `aspiration` (`NATURALLY_ASPIRATED`, `TURBOCHARGED`, `SUPERCHARGED`), `power_hp`, `torque_nm`.
8. **`ElectricMotor`**: `id` (PK), `motor_type` (`PERMANENT_MAGNET_SYNC`, `INDUCTION`), `position` (`FRONT_AXLE`, `REAR_AXLE`, `HUB`), `power_kw`, `torque_nm`, `max_rpm`.
9. **`Battery`**: `id` (PK), `chemistry` (`NMC`, `LFP`, `SOLID_STATE`), `gross_capacity_kwh`, `usable_capacity_kwh`, `nominal_voltage`, `max_dc_charge_kw`, `max_ac_charge_kw`, `charge_time_10_80_min`, `thermal_management` (`LIQUID_COOLED`, `AIR_COOLED`).
10. **`Transmission`**: `id` (PK), `transmission_type` (`MANUAL`, `TORQUE_CONVERTER_AUTO`, `DUAL_CLUTCH`, `CVT`, `SINGLE_SPEED_REDUCER`), `gear_count`.

### 2.3 Market Localization Aggregate
11. **`Market`**: `id` (PK), `code` (Unique: `MAR`, `FRA`, `ARE`), `name`, `currency_code` (`MAD`, `EUR`, `AED`), `default_locale` (`fr-MA`, `ar-MA`).
12. **`Country`**: `id` (PK), `iso_code_2` (`MA`, `FR`), `iso_code_3` (`MAR`, `FRA`), `name`, `currency_symbol`, `dialing_code`.
13. **`ModelYear`**: `id` (PK), `year` (Integer).
14. **`MarketAvailability`**: `id` (PK), `product_id` (FK), `market_id` (FK), `model_year_id` (FK), `local_trim_name` (e.g. `Dynamic+`), `msrp_base_price`, `currency_code`, `fiscal_horsepower_cv`, `annual_vignette_tax`, `warranty_years`, `warranty_km`, `is_orderable`, `effective_date`. *(Unique Constraint on product_id + market_id + model_year_id)*.

### 2.4 Marketplace Listings & Users
15. **`User`**: `id` (PK), `uuid`, `email`, `phone`, `password_hash`, `role`, `status`, `locale`, `country_code`, `created_at`, `deleted_at`.
16. **`Dealer`**: `id` (PK), `uuid`, `name`, `slug`, `type` (`AUTHORIZED_FRANCHISE`, `INDEPENDENT`), `city`, `phone`, `email`, `is_verified`, `subscription_tier`.
17. **`MarketplaceListing`**: `id` (PK), `uuid`, `seller_type` (`PRIVATE`, `DEALER`), `user_id` (FK), `dealer_id` (FK, Nullable), `market_availability_id` (FK, Nullable), `brand_name`, `model_name`, `year`, `mileage_km`, `price`, `currency_code`, `condition` (`USED`, `CERTIFIED_PREOWNED`, `NEW`), `city`, `status` (`DRAFT`, `PENDING`, `ACTIVE`, `SOLD`, `ARCHIVED`), `is_vin_verified`, `is_seller_verified`, `is_inspection_verified`, `search_vector` (tsvector), `published_at`.
18. **`ListingImage`**: `id` (PK), `listing_id` (FK), `image_url`, `display_order`, `is_primary`.

### 2.5 Profile, Garage, & Reviews
19. **`MobilityProfile`**: `id` (PK), `user_id` (FK, Unique), `country_code`, `city`, `budget_min`, `budget_max`, `max_monthly_payment`, `daily_mileage_km`, `annual_mileage_km`, `city_driving_pct`, `highway_driving_pct`, `passenger_count`, `has_home_charging`, `priority_weights` (JSONB), `category_facets` (JSONB).
20. **`GarageVehicle`**: `id` (PK), `user_id` (FK), `market_availability_id` (FK, Nullable), `custom_name`, `vin`, `registration_number`, `current_mileage_km`, `purchase_date`, `purchase_price`.
21. **`Review`**: `id` (PK), `user_id` (FK), `product_id` (FK), `ownership_duration_months`, `real_world_consumption`, `overall_rating` (1–10), `reliability_rating`, `comfort_rating`, `performance_rating`, `review_text`, `status` (`PENDING`, `APPROVED`, `FLAGGED`).

### 2.6 Decision Engine, Reconstructability, & Provenance
22. **`ScoreInputSnapshot`**: `id` (PK), `snapshot_hash` (SHA-256), `raw_payload_json` (JSONB), `created_at`.
23. **`ScoreResult`**: `id` (PK), `entity_type`, `entity_id`, `score_type` (`GLOBAL_WANAIA`, `PERSONAL_FIT`, `DEAL_SCORE`), `algorithm_version`, `score_value` (Decimal 0–100), `rating_class`, `confidence` (0.00–1.00), `input_snapshot_id` (FK), `dimension_breakdown` (JSONB), `calculated_at`.
24. **`ScoreExplanationItem`**: `id` (PK), `score_result_id` (FK), `type` (`PRO`, `CON`, `WARNING`), `category`, `code`, `message_template`, `parameters` (JSONB), `provenance_ref`.
25. **`RecommendationTrace`**: `id` (PK), `user_id` (FK), `market_code`, `algorithm_version`, `profile_snapshot_json` (JSONB), `candidate_product_ids` (BIGINT[]), `ranked_product_ids` (BIGINT[]), `score_map` (JSONB), `explanations_map` (JSONB), `generated_at`.
26. **`DataSource`**: `id` (PK), `code`, `name`, `source_type` (`MANUFACTURER`, `REGULATOR`, `INDEPENDENT_TEST`, `DEALER`, `COMMUNITY`), `trust_tier`, `created_at`.
27. **`AttributeProvenance`**: `id` (PK), `entity_type`, `entity_id`, `attribute_name`, `epistemic_type` (`FACT`, `OBSERVATION`, `CALCULATION`, `INTELLIGENCE`, `EDITORIAL_OPINION`), `source_id` (FK), `source_reference`, `collected_at`, `valid_from`, `valid_to`, `market_code`, `verification_status`, `confidence_level`.

---

*This conceptual database model satisfies all DDD isolation requirements, supports all modern propulsion architectures, and guarantees total auditability.*
