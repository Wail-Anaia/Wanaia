-- ============================================================
-- V6: Clearly Marked Development Seed Data
-- ============================================================

-- 1. Mobility Categories
INSERT INTO mobility_categories (id, code, name_en, name_fr, name_ar, icon_url, display_order, is_active)
VALUES
  (1, 'CAR', 'Cars & Passenger Vehicles', 'Voitures Particulières', 'سيارات الركاب', 'https://assets.wanaia.com/icons/car.svg', 1, TRUE),
  (2, 'MOTORCYCLE', 'Motorcycles & Scooters', 'Motos & Scooters', 'دراجات نارية وسكوتر', 'https://assets.wanaia.com/icons/moto.svg', 2, TRUE),
  (3, 'COMMERCIAL_VAN', 'Vans & Commercial Fleet', 'Utilitaires & Flottes', 'شاحنات خفيفة ونفعية', 'https://assets.wanaia.com/icons/van.svg', 3, TRUE);

-- 2. Brands
INSERT INTO brands (id, category_id, name, slug, logo_url, country_of_origin, founded_year, is_active, display_order)
VALUES
  (1, 1, 'Toyota', 'toyota', 'https://assets.wanaia.com/brands/toyota.png', 'JPN', 1937, TRUE, 1),
  (2, 1, 'Renault', 'renault', 'https://assets.wanaia.com/brands/renault.png', 'FRA', 1899, TRUE, 2),
  (3, 1, 'Tesla', 'tesla', 'https://assets.wanaia.com/brands/tesla.png', 'USA', 2003, TRUE, 3),
  (4, 2, 'Yamaha', 'yamaha', 'https://assets.wanaia.com/brands/yamaha.png', 'JPN', 1955, TRUE, 4);

-- 3. Models
INSERT INTO models (id, brand_id, name, slug, segment_code, is_active)
VALUES
  (1, 1, 'RAV4', 'rav4', 'C_SUV', TRUE),
  (2, 2, 'Clio', 'clio', 'B_HATCHBACK', TRUE),
  (3, 3, 'Model Y', 'model-y', 'D_SUV', TRUE),
  (4, 4, 'MT-07', 'mt-07', 'NAKED_MIDWEIGHT', TRUE);

-- 4. Generations
INSERT INTO generations (id, model_id, name, slug, internal_platform_code, start_year, end_year, is_current)
VALUES
  (1, 1, '5th Generation (XA50)', '2018-xa50', 'XA50', 2018, NULL, TRUE),
  (2, 2, '5th Generation (Clio V)', '2019-clio-5', 'BJA', 2019, NULL, TRUE),
  (3, 3, '1st Generation (Model Y)', '2020-model-y', 'GEN1', 2020, NULL, TRUE),
  (4, 4, '3rd Generation (MT-07)', '2021-mt07', 'RM33', 2021, NULL, TRUE);

-- 5. Powertrain Components (Engines, Motors, Batteries, Transmissions)
INSERT INTO engines (id, engine_code, fuel_type, displacement_cc, cylinders, power_hp, torque_nm)
VALUES
  (1, 'A25A-FXS', 'PETROL', 2487, 4, 178, 221),
  (2, 'H4Dt', 'PETROL', 999, 3, 90, 160),
  (3, 'CP2', 'PETROL', 689, 2, 73, 67);

INSERT INTO electric_motors (id, motor_code, motor_type, position, power_kw, torque_nm)
VALUES
  (1, '3NM', 'PERMANENT_MAGNET_SYNCHRONOUS', 'FRONT_AXLE', 88, 202),
  (2, '4NM', 'PERMANENT_MAGNET_SYNCHRONOUS', 'REAR_AXLE', 40, 121),
  (3, '3D1-3D6', 'PERMANENT_MAGNET_SYNCHRONOUS', 'REAR_AXLE', 220, 440);

INSERT INTO batteries (id, battery_code, chemistry, usable_capacity_kwh, max_dc_charge_kw, charge_time_10_80_min)
VALUES
  (1, 'THS-BAT-1.6', 'NMC', 1.60, NULL, NULL),
  (2, 'TESLA-LG-NMC', 'NMC', 75.00, 250, 27);

INSERT INTO transmissions (id, name, transmission_type, gear_count)
VALUES
  (1, 'e-CVT Toyota THS-II', 'CVT', 1),
  (2, 'BVM6 Renault', 'MANUAL', 6),
  (3, 'Single-Speed Fixed Ratio Reducer', 'SINGLE_SPEED_REDUCER', 1),
  (4, '6-Speed Constant Mesh', 'MANUAL', 6);

-- 6. Powertrain Configurations
INSERT INTO powertrain_configurations (
  id, code, propulsion_type, primary_fuel, drive_layout, engine_id, primary_motor_id, secondary_motor_id,
  battery_id, transmission_id, combined_power_hp, combined_power_kw, combined_torque_nm,
  acceleration_0_100_s, top_speed_kmh, wltp_consumption_metric, wltp_co2_gkm, ev_range_wltp_km
) VALUES
  (1, 'TOYOTA_THS_2.5_AWD', 'HEV', 'PETROL', 'AWD', 1, 1, 2, 1, 1, 222, 163, 221, 8.10, 180, 5.7, 128, NULL),
  (2, 'RENAULT_TCe_90_BVM6', 'ICE', 'PETROL', 'FWD', 2, NULL, NULL, NULL, 2, 90, 67, 160, 12.20, 180, 5.2, 118, NULL),
  (3, 'TESLA_MODEL_Y_LR_AWD', 'BEV', 'ELECTRICITY', 'AWD', NULL, 3, 3, 2, 3, 514, 378, 493, 5.00, 217, 16.9, 0, 533),
  (4, 'YAMAHA_CP2_689', 'ICE', 'PETROL', 'CHAIN_DRIVE', 3, NULL, NULL, NULL, 4, 73, 54, 67, 4.20, 205, 4.2, 98, NULL);

-- 7. Mobility Products
INSERT INTO mobility_products (
  id, uuid, generation_id, variant_name, slug, body_style, powertrain_config_id,
  curb_weight_kg, length_mm, width_mm, height_mm, wheelbase_mm, boot_capacity_liters, seat_count, safety_rating_ncap, is_active
) VALUES
  (1, '11111111-1111-1111-1111-111111111111', 1, '2.5 Hybrid AWD-i', '2-5-hybrid-awd-i', 'SUV', 1, 1650, 4600, 1855, 1685, 2690, 580, 5, 5.0, TRUE),
  (2, '22222222-2222-2222-2222-222222222222', 2, 'TCe 90 BVM6', 'tce-90-bvm6', 'HATCHBACK', 2, 1100, 4050, 1798, 1440, 2583, 391, 5, 5.0, TRUE),
  (3, '33333333-3333-3333-3333-333333333333', 3, 'Long Range Dual Motor', 'long-range-dual-motor', 'SUV', 3, 1979, 4751, 1921, 1624, 2890, 854, 5, 5.0, TRUE),
  (4, '44444444-4444-4444-4444-444444444444', 4, 'MT-07 Standard', 'standard', 'NAKED', 4, 184, 2085, 780, 1105, 1400, NULL, 2, NULL, TRUE);

-- 8. Markets & Countries & Model Years
INSERT INTO markets (id, code, name, currency_code, default_locale, is_active)
VALUES
  (1, 'MAR', 'Kingdom of Morocco', 'MAD', 'fr-MA', TRUE),
  (2, 'FRA', 'French Republic', 'EUR', 'fr-FR', TRUE);

INSERT INTO countries (id, iso_code_2, iso_code_3, name, currency_symbol, dialing_code)
VALUES
  (1, 'MA', 'MAR', 'Morocco', 'DH', '+212'),
  (2, 'FR', 'FRA', 'France', '€', '+33');

INSERT INTO model_years (id, year)
VALUES
  (1, 2024),
  (2, 2025),
  (3, 2026);

-- 9. Market Availabilities (Morocco & France)
INSERT INTO market_availabilities (
  id, product_id, market_id, model_year_id, local_trim_name, msrp_base_price, currency_code,
  fiscal_horsepower_cv, annual_vignette_tax, warranty_years, warranty_km, is_orderable, effective_date
) VALUES
  (1, 1, 1, 2, 'Dynamic+ AWD', 415000.00, 'MAD', 9, 3000.00, 3, 100000, TRUE, '2025-01-01'),
  (2, 1, 2, 2, 'Lounge AWD', 48950.00, 'EUR', 9, 0.00, 3, 100000, TRUE, '2025-01-01'),
  (3, 2, 1, 2, 'Equilibre', 165000.00, 'MAD', 6, 650.00, 3, 100000, TRUE, '2025-01-01'),
  (4, 3, 1, 2, 'Long Range Dual Motor', 590000.00, 'MAD', 0, 0.00, 4, 80000, TRUE, '2025-01-01');

-- 10. Data Sources & Attribute Provenance
INSERT INTO data_sources (id, code, name, source_type, trust_tier, website_url)
VALUES
  (1, 'OEM_TOYOTA_MA', 'Toyota du Maroc Homologation', 'MANUFACTURER', 1, 'https://www.toyota.ma'),
  (2, 'OEM_RENAULT_MA', 'Renault Maroc Official Catalog', 'MANUFACTURER', 1, 'https://www.renault.ma'),
  (3, 'EURO_NCAP', 'European New Car Assessment Programme', 'REGULATOR', 1, 'https://www.euroncap.com');

INSERT INTO attribute_provenance (
  id, entity_type, entity_id, attribute_name, epistemic_type, source_id, source_reference,
  collected_at, valid_from, market_code, verification_status, confidence_level
) VALUES
  (1, 'MOBILITY_PRODUCT', 1, 'safety_rating_ncap', 'FACT', 3, 'Euro NCAP 2019 Official Test 5 Stars', NOW(), '2019-01-01', 'MAR', 'OFFICIALLY_VERIFIED', 'CONFIRMED_HIGH'),
  (2, 'POWERTRAIN', 1, 'wltp_consumption_metric', 'FACT', 1, 'Homologation Sheet #MA-2025-TY-712', NOW(), '2025-01-01', 'MAR', 'OFFICIALLY_VERIFIED', 'CONFIRMED_HIGH');

SELECT setval('mobility_categories_id_seq', (SELECT MAX(id) FROM mobility_categories));
SELECT setval('brands_id_seq', (SELECT MAX(id) FROM brands));
SELECT setval('models_id_seq', (SELECT MAX(id) FROM models));
SELECT setval('generations_id_seq', (SELECT MAX(id) FROM generations));
SELECT setval('engines_id_seq', (SELECT MAX(id) FROM engines));
SELECT setval('electric_motors_id_seq', (SELECT MAX(id) FROM electric_motors));
SELECT setval('batteries_id_seq', (SELECT MAX(id) FROM batteries));
SELECT setval('transmissions_id_seq', (SELECT MAX(id) FROM transmissions));
SELECT setval('powertrain_configurations_id_seq', (SELECT MAX(id) FROM powertrain_configurations));
SELECT setval('mobility_products_id_seq', (SELECT MAX(id) FROM mobility_products));
SELECT setval('markets_id_seq', (SELECT MAX(id) FROM markets));
SELECT setval('countries_id_seq', (SELECT MAX(id) FROM countries));
SELECT setval('model_years_id_seq', (SELECT MAX(id) FROM model_years));
SELECT setval('market_availabilities_id_seq', (SELECT MAX(id) FROM market_availabilities));
SELECT setval('data_sources_id_seq', (SELECT MAX(id) FROM data_sources));
SELECT setval('attribute_provenance_id_seq', (SELECT MAX(id) FROM attribute_provenance));
