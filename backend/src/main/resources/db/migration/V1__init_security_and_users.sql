-- ============================================================
-- V1: User Security, Accounts, and Refresh Token Revocation
-- ============================================================

CREATE TABLE users (
    id                  BIGSERIAL PRIMARY KEY,
    uuid                UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    email               VARCHAR(255) NOT NULL UNIQUE,
    phone               VARCHAR(30),
    password_hash       VARCHAR(255) NOT NULL,
    first_name          VARCHAR(100),
    last_name           VARCHAR(100),
    display_name        VARCHAR(100),
    role                VARCHAR(30) NOT NULL DEFAULT 'USER',
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    email_verified      BOOLEAN NOT NULL DEFAULT FALSE,
    phone_verified      BOOLEAN NOT NULL DEFAULT FALSE,
    locale              VARCHAR(10) DEFAULT 'fr-MA',
    country_code        VARCHAR(3) DEFAULT 'MA',
    last_login_at       TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_uuid ON users(uuid);

CREATE TABLE refresh_tokens (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash          VARCHAR(255) NOT NULL UNIQUE,
    client_type         VARCHAR(20) NOT NULL DEFAULT 'WEB',
    device_info         VARCHAR(500),
    ip_address          VARCHAR(45),
    expires_at          TIMESTAMPTZ NOT NULL,
    revoked_at          TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_hash ON refresh_tokens(token_hash);
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);
