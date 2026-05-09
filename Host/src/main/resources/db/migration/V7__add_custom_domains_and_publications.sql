-- -----------------------------------------------------------------------------
-- Migration: V7__add_custom_domains_and_publications.sql
-- Description: Custom domain registration + CMS content publication schema
-- -----------------------------------------------------------------------------

-- 1) Custom domains for user-owned domain publishing
CREATE TABLE custom_domains (
    domain_id           CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    user_id             CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    domain_name         VARCHAR(253) COLLATE utf8mb4_unicode_ci NOT NULL,
    verification_token  VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
    verification_status VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
    dns_status          VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
    ssl_status          VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
    is_active           TINYINT(1) NOT NULL DEFAULT 0,
    created_date        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_date        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    PRIMARY KEY (domain_id),
    UNIQUE KEY uq_custom_domains_domain_name (domain_name),
    KEY ix_custom_domains_user_id (user_id),

    CONSTRAINT ck_custom_domains_verification_status
        CHECK (verification_status IN ('PENDING', 'VERIFIED', 'FAILED')),
    CONSTRAINT ck_custom_domains_dns_status
        CHECK (dns_status IN ('PENDING', 'POINTED', 'FAILED')),
    CONSTRAINT ck_custom_domains_ssl_status
        CHECK (ssl_status IN ('PENDING', 'ACTIVE', 'FAILED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 2) CMS content publications linking content to custom domains
CREATE TABLE cms_content_publications (
    publication_id  CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    content_id      CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    user_id         CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    domain_id       CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    slug            VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    canonical_url   VARCHAR(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    summary         TEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    cover_image_url VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    published_date  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    is_active       TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (publication_id),
    KEY ix_publications_domain_content (domain_id, content_id),
    KEY ix_publications_user_id (user_id),
    UNIQUE KEY uq_publications_domain_slug (domain_id, slug)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
