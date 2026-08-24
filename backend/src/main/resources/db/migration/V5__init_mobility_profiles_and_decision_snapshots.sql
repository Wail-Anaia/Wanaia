-- ============================================================
-- V5: User Mobility Profiles, Score Snapshots, and Recommendation Traces
-- ============================================================

CREATE TABLE mobility_profiles (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    country_code        VARCHAR(3) NOT NULL DEFAULT 'MA',
    city                VARCHAR(100),
    preferred_currency  VARCHAR(3) NOT NULL DEFAULT 'MAD',
    budget_min          DECIMAL(12,2),
    budget_max          DECIMAL(12,2),
    max_monthly_payment DECIMAL(12,2),
    condition_preference VARCHAR(30) DEFAULT 'ANY',
    ownership_horizon_years INTEGER DEFAULT 5,
    daily_mileage_km    INTEGER DEFAULT 30,
    annual_mileage_km   INTEGER DEFAULT 12000,
    city_driving_pct    INTEGER DEFAULT 60,
    highway_driving_pct INTEGER DEFAULT 40,
    primary_usage       VARCHAR(50) DEFAULT 'DAILY_COMMUTE',
    typical_passenger_count INTEGER DEFAULT 2,
    isofix_seats_required INTEGER DEFAULT 0,
    has_home_charging   BOOLEAN DEFAULT FALSE,
    has_work_charging   BOOLEAN DEFAULT FALSE,
    parking_type        VARCHAR(30) DEFAULT 'STREET',
    rough_road_needs    BOOLEAN DEFAULT FALSE,
    priority_reliability INTEGER DEFAULT 4,
    priority_fuel_economy INTEGER DEFAULT 4,
    priority_comfort    INTEGER DEFAULT 3,
    priority_performance INTEGER DEFAULT 3,
    priority_safety     INTEGER DEFAULT 4,
    priority_resale     INTEGER DEFAULT 3,
    car_facet_json      TEXT,
    motorcycle_facet_json TEXT,
    commercial_facet_json TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_mobility_profiles_user ON mobility_profiles(user_id);

CREATE TABLE score_input_snapshots (
    id                  BIGSERIAL PRIMARY KEY,
    snapshot_hash       VARCHAR(64) NOT NULL,
    raw_payload_json    TEXT NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_snapshot_hash ON score_input_snapshots(snapshot_hash);

CREATE TABLE score_results (
    id                  BIGSERIAL PRIMARY KEY,
    entity_type         VARCHAR(50) NOT NULL,
    entity_id           BIGINT NOT NULL,
    score_type          VARCHAR(50) NOT NULL,
    algorithm_version   VARCHAR(30) NOT NULL,
    score_value         DECIMAL(5,2) NOT NULL,
    rating_class        VARCHAR(30) NOT NULL,
    confidence_level    DECIMAL(3,2) NOT NULL DEFAULT 0.95,
    input_snapshot_id   BIGINT NOT NULL REFERENCES score_input_snapshots(id),
    dimension_breakdown_json TEXT,
    calculated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_scores_lookup ON score_results(entity_type, entity_id, score_type);

CREATE TABLE score_explanation_items (
    id                  BIGSERIAL PRIMARY KEY,
    score_result_id     BIGINT NOT NULL REFERENCES score_results(id) ON DELETE CASCADE,
    type                VARCHAR(20) NOT NULL, -- 'PRO', 'CON', 'WARNING'
    category            VARCHAR(50) NOT NULL,
    code                VARCHAR(80) NOT NULL,
    message_template    VARCHAR(500) NOT NULL,
    parameters_json     TEXT,
    provenance_ref      VARCHAR(255),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_explanations_score_result ON score_explanation_items(score_result_id);

CREATE TABLE recommendation_traces (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT REFERENCES users(id) ON DELETE SET NULL,
    market_code         VARCHAR(3) NOT NULL DEFAULT 'MAR',
    algorithm_version   VARCHAR(30) NOT NULL,
    profile_snapshot_json TEXT NOT NULL,
    candidate_product_ids_json TEXT NOT NULL,
    ranked_product_ids_json TEXT NOT NULL,
    scores_map_json     TEXT NOT NULL,
    explanations_map_json TEXT NOT NULL,
    generated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_rec_traces_user ON recommendation_traces(user_id);
