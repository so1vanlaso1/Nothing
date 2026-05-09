-- -----------------------------------------------------------------------------
-- Migration: V6__add_social_media_connections_table.sql
-- Description: Stores social media credentials and page IDs for users
-- -----------------------------------------------------------------------------

CREATE TABLE social_media_connections (
    id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    platform VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    access_token TEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    long_lived_user_token VARCHAR(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Encrypted ~60-day user token used to mint fresh page tokens',
    page_id VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    token_expiry_date DATETIME(6) DEFAULT NULL,
    token_last_refreshed DATETIME(6) DEFAULT NULL, -- Cột mới thêm vào trực tiếp
    created_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY ux_social_media_connections_user_platform (user_id, platform),
    KEY ix_social_media_connections_user_id (user_id),
    KEY ix_social_media_connections_platform (platform),
    CONSTRAINT fk_social_media_connections_user FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;