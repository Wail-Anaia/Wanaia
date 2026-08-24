-- ============================================================
-- V3: Sovereign Markets, Countries, Model Years, and Market Availability
-- ============================================================

CREATE TABLE markets (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(3) NOT NULL UNIQUE, -- 'MAR', 'FRA', 'ARE'
    name                VARCHAR(100) NOT NULL,
    currency_code       VARCHAR(3) NOT NULL, -- 'MAD', 'EUR', 'AED'
    default_locale      VARCHAR(10) NOT NULL DEFAULT 'fr-MA',
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE countries (
    id                  BIGSERIAL PRIMARY KEY,
    iso_code_2          VARCHAR(2) NOT NULL UNIQUE, -- 'MA', 'FR', 'AE'
    iso_code_3          VARCHAR(3) NOT NULL UNIQUE, -- 'MAR', 'FRA', 'ARE'
    name                VARCHAR(100) NOT NULL,
    currency_symbol     VARCHAR(10) DEFAULT 'DH',
    dialing_code        VARCHAR(10) DEFAULT '+212',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE model_years (
    id                  BIGSERIAL PRIMARY KEY,
    year                INTEGER NOT NULL UNIQUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE market_availabilities (
    id                  BIGSERIAL PRIMARY KEY,
    product_id          BIGINT NOT NULL REFERENCES mobility_products(id),
    market_id           BIGINT NOT NULL REFERENCES markets(id),
    model_year_id       BIGINT NOT NULL REFERENCES model_years(id),
    local_trim_name     VARCHAR(150) NOT NULL,
    msrp_base_price     DECIMAL(12,2) NOT NULL,
    currency_code       VARCHAR(3) NOT NULL,
    fiscal_horsepower_cv INTEGER,
    annual_vignette_tax DECIMAL(10,2),
    warranty_years      INTEGER DEFAULT 3,
    warranty_km         INTEGER DEFAULT 100000,
    is_orderable        BOOLEAN NOT NULL DEFAULT TRUE,
    effective_date      DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_product_market_year UNIQUE (product_id, market_id, model_year_id)
);

CREATE INDEX idx_avail_product ON market_availabilities(product_id);
CREATE INDEX idx_avail_market ON market_availabilities(market_id);
