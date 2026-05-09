-- -----------------------------------------------------------------------------
-- Migration: V1__init_core_auth_tables.sql
-- Description: Production-ready auth/profile schema (snake_case standard)
-- -----------------------------------------------------------------------------

-- 1) Core identity (authentication only)
CREATE TABLE auth_users (
    user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    email VARCHAR(128) COLLATE utf8mb4_unicode_ci NOT NULL,
    email_normalized VARCHAR(128) COLLATE utf8mb4_unicode_ci GENERATED ALWAYS AS (LOWER(TRIM(email))) STORED,
    password_hash VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 0,
    is_email_verified TINYINT(1) NOT NULL DEFAULT 0,
    create_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    update_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (user_id),
    UNIQUE KEY ix_auth_users_email_normalized (email_normalized),
    KEY ix_auth_users_is_active (is_active),
    CONSTRAINT ck_auth_users_is_active CHECK (is_active IN (0, 1)),
    CONSTRAINT ck_auth_users_is_email_verified CHECK (is_email_verified IN (0, 1))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 2) User profile (business/domain data only)
CREATE TABLE user_profiles (
    user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    first_name VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    last_name VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    avatar_url VARCHAR(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    create_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    update_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (user_id),
    CONSTRAINT fk_user_profiles_auth_users FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 3) Roles
CREATE TABLE auth_roles (
    role_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    name VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    description VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    create_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (role_id),
    UNIQUE KEY ix_auth_roles_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 4) User-role mapping
CREATE TABLE auth_user_roles (
    user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    role_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    assigned_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (user_id, role_id),
    KEY ix_auth_user_roles_role_id (role_id),
    CONSTRAINT fk_auth_user_roles_user FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_auth_user_roles_role FOREIGN KEY (role_id) REFERENCES auth_roles (role_id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 5) Refresh tokens (store hash, not raw token)
CREATE TABLE auth_refresh_tokens (
    token_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    token_hash CHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
    jti CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    issued_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    expires_at DATETIME(6) NOT NULL,
    is_revoked TINYINT(1) NOT NULL DEFAULT 0,
    revoked_at DATETIME(6) DEFAULT NULL,
    replaced_by_token_id CHAR(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    create_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (token_id),
    UNIQUE KEY ix_auth_refresh_tokens_token_hash (token_hash),
    UNIQUE KEY ix_auth_refresh_tokens_jti (jti),
    KEY ix_auth_refresh_tokens_user_id (user_id),
    KEY ix_auth_refresh_tokens_expires_at (expires_at),
    KEY ix_auth_refresh_tokens_user_revoked_expires (
        user_id,
        is_revoked,
        expires_at
    ),
    CONSTRAINT fk_auth_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_auth_refresh_tokens_replaced_by FOREIGN KEY (replaced_by_token_id) REFERENCES auth_refresh_tokens (token_id) ON DELETE SET NULL,
    CONSTRAINT ck_auth_refresh_tokens_is_revoked CHECK (is_revoked IN (0, 1)),
    CONSTRAINT ck_auth_refresh_tokens_expiry CHECK (expires_at > issued_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS password_reset_tokens (
  token_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  token_hash VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  expires_at DATETIME(6) NOT NULL,
  used TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  used_at DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (token_id),
  KEY idx_token_hash (token_hash),
  KEY idx_user_id_expires (user_id, expires_at),
  KEY idx_prt_expires_at (expires_at),
  CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE EVENT IF NOT EXISTS ev_cleanup_expired_tokens
ON SCHEDULE EVERY 1 DAY
DO
  DELETE FROM password_reset_tokens
  WHERE expires_at < NOW()
    OR (used = 1 AND used_at < NOW() - INTERVAL 1 DAY);