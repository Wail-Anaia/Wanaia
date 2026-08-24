-- ============================================================
-- V2: Mobility Taxonomy, Decoupled Powertrains, and Products
-- ============================================================

CREATE TABLE mobility_categories (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(30) NOT NULL UNIQUE, -- 'CAR', 'MOTORCYCLE', 'SCOOTER', 'COMMERCIAL_VAN', 'TRUCK', 'E_BIKE'
    name_en             VARCHAR(50) NOT NULL,
    name_fr             VARCHAR(50) NOT NULL,
    name_ar             VARCHAR(50) NOT NULL,
    icon_url            VARCHAR(500),
    display_order       INTEGER DEFAULT 0,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE brands (
    id                  BIGSERIAL PRIMARY KEY,
    category_id         BIGINT NOT NULL REFERENCES mobility_categories(id),
    name                VARCHAR(100) NOT NULL,
    slug                VARCHAR(100) NOT NULL,
    logo_url            VARCHAR(500),
    country_of_origin   VARCHAR(3), -- ISO-3166-1 alpha-3
    founded_year        INTEGER,
    description         TEXT,
    website_url         VARCHAR(500),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    display_order       INTEGER DEFAULT 0,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_brand_category_slug UNIQUE (category_id, slug)
);

CREATE INDEX idx_brands_slug ON brands(slug);
CREATE INDEX idx_brands_category ON brands(category_id);

CREATE TABLE models (
    id                  BIGSERIAL PRIMARY KEY,
    brand_id            BIGINT NOT NULL REFERENCES brands(id),
    name                VARCHAR(100) NOT NULL,
    slug                VARCHAR(150) NOT NULL,
    segment_code        VARCHAR(50),
    description         TEXT,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_model_brand_slug UNIQUE (brand_id, slug)
);

CREATE INDEX idx_models_brand ON models(brand_id);
CREATE INDEX idx_models_slug ON models(slug);

CREATE TABLE generations (
    id                  BIGSERIAL PRIMARY KEY,
    model_id            BIGINT NOT NULL REFERENCES models(id),
    name                VARCHAR(100) NOT NULL,
    slug                VARCHAR(150) NOT NULL,
    internal_platform_code VARCHAR(50),
    start_year          INTEGER NOT NULL,
    end_year            INTEGER,
    hero_image_url      VARCHAR(500),
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_gen_model_slug UNIQUE (model_id, slug)
);

CREATE INDEX idx_generations_model ON generations(model_id);

-- Powertrain Components (Decoupled Physical Assets)

CREATE TABLE engines (
    id                  BIGSERIAL PRIMARY KEY,
    engine_code         VARCHAR(50),
    fuel_type           VARCHAR(30) NOT NULL, -- 'PETROL', 'DIESEL', 'LPG'
    displacement_cc     INTEGER NOT NULL,
    cylinders           INTEGER NOT NULL,
    valves_per_cylinder INTEGER DEFAULT 4,
    aspiration          VARCHAR(30) DEFAULT 'TURBOCHARGED',
    power_hp            INTEGER NOT NULL,
    torque_nm           INTEGER NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE electric_motors (
    id                  BIGSERIAL PRIMARY KEY,
    motor_code          VARCHAR(50),
    motor_type          VARCHAR(40) DEFAULT 'PERMANENT_MAGNET_SYNCHRONOUS',
    position            VARCHAR(30) DEFAULT 'FRONT_AXLE',
    power_kw            INTEGER NOT NULL,
    torque_nm           INTEGER NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE batteries (
    id                  BIGSERIAL PRIMARY KEY,
    battery_code        VARCHAR(50),
    chemistry           VARCHAR(30) DEFAULT 'NMC',
    gross_capacity_kwh  DECIMAL(6,2),
    usable_capacity_kwh DECIMAL(6,2),
    nominal_voltage     INTEGER DEFAULT 400,
    max_dc_charge_kw    INTEGER,
    max_ac_charge_kw    INTEGER DEFAULT 11,
    charge_time_10_80_min INTEGER,
    thermal_management  VARCHAR(30) DEFAULT 'LIQUID_COOLED',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE transmissions (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    transmission_type   VARCHAR(40) NOT NULL, -- 'MANUAL', 'TORQUE_CONVERTER_AUTO', 'DUAL_CLUTCH', 'CVT', 'SINGLE_SPEED_REDUCER'
    gear_count          INTEGER DEFAULT 1,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE powertrain_configurations (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(80) NOT NULL UNIQUE,
    propulsion_type     VARCHAR(30) NOT NULL, -- 'ICE', 'MHEV', 'HEV', 'PHEV', 'BEV', 'FCEV'
    primary_fuel        VARCHAR(30) NOT NULL, -- 'PETROL', 'DIESEL', 'ELECTRICITY', 'HYDROGEN'
    drive_layout        VARCHAR(20) NOT NULL DEFAULT 'FWD',
    engine_id           BIGINT REFERENCES engines(id),
    primary_motor_id    BIGINT REFERENCES electric_motors(id),
    secondary_motor_id  BIGINT REFERENCES electric_motors(id),
    battery_id          BIGINT REFERENCES batteries(id),
    transmission_id     BIGINT REFERENCES transmissions(id),
    combined_power_hp   INTEGER NOT NULL,
    combined_power_kw   INTEGER NOT NULL,
    combined_torque_nm  INTEGER NOT NULL,
    acceleration_0_100_s DECIMAL(4,2),
    top_speed_kmh       INTEGER,
    wltp_consumption_metric DECIMAL(4,1),
    wltp_co2_gkm        INTEGER,
    ev_range_wltp_km    INTEGER,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Master Mobility Product Catalog

CREATE TABLE mobility_products (
    id                  BIGSERIAL PRIMARY KEY,
    uuid                UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    generation_id       BIGINT NOT NULL REFERENCES generations(id),
    variant_name        VARCHAR(150) NOT NULL,
    slug                VARCHAR(200) NOT NULL,
    body_style          VARCHAR(50) NOT NULL,
    powertrain_config_id BIGINT NOT NULL REFERENCES powertrain_configurations(id),
    curb_weight_kg      INTEGER,
    length_mm           INTEGER,
    width_mm            INTEGER,
    height_mm           INTEGER,
    wheelbase_mm        INTEGER,
    boot_capacity_liters INTEGER,
    seat_count          INTEGER DEFAULT 5,
    safety_rating_ncap  DECIMAL(3,1),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_product_gen_slug UNIQUE (generation_id, slug)
);

CREATE INDEX idx_products_generation ON mobility_products(generation_id);
CREATE INDEX idx_products_slug ON mobility_products(slug);
CREATE INDEX idx_products_uuid ON mobility_products(uuid);
