-- ============================================================
-- V4: Data Sources, Epistemic Classifications, and Provenance Audit
-- ============================================================

CREATE TABLE data_sources (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(50) NOT NULL UNIQUE,
    name                VARCHAR(150) NOT NULL,
    source_type         VARCHAR(50) NOT NULL, -- 'MANUFACTURER', 'REGULATOR', 'INDEPENDENT_TEST', 'DEALER', 'COMMUNITY'
    trust_tier          INTEGER NOT NULL DEFAULT 1,
    website_url         VARCHAR(500),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE attribute_provenance (
    id                  BIGSERIAL PRIMARY KEY,
    entity_type         VARCHAR(50) NOT NULL,
    entity_id           BIGINT NOT NULL,
    attribute_name      VARCHAR(100) NOT NULL,
    epistemic_type      VARCHAR(30) NOT NULL, -- 'FACT', 'OBSERVATION', 'CALCULATION', 'INTELLIGENCE', 'EDITORIAL_OPINION', 'USER_GENERATED', 'AI_EXPLANATION'
    source_id           BIGINT REFERENCES data_sources(id),
    source_reference    VARCHAR(255),
    collected_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    valid_from          DATE NOT NULL DEFAULT CURRENT_DATE,
    valid_to            DATE,
    market_code         VARCHAR(3) NOT NULL DEFAULT 'MAR',
    verification_status VARCHAR(30) NOT NULL DEFAULT 'OFFICIALLY_VERIFIED',
    confidence_level    VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED_HIGH',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_attr_provenance_lookup ON attribute_provenance(entity_type, entity_id, attribute_name);
