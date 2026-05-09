-- -----------------------------------------------------------------------------
-- Migration: V3__init_marketing_and_cms_tables.sql
-- Description: Marketing generation + CMS content schema
-- -----------------------------------------------------------------------------

-- 1) Marketing generation history
CREATE TABLE marketing_generations (
    generation_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,

    content_type VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    platform VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    target_audience VARCHAR(265) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    tone VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    goal VARCHAR(265) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    product_name VARCHAR(265) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    brand_name VARCHAR(265) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    prompt_input TEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    generated_content LONGTEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    status VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'COMPLETED',
    error_message VARCHAR(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    create_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    update_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    PRIMARY KEY (generation_id),
    KEY ix_marketing_generations_user_id (user_id),
    KEY ix_marketing_generations_content_type (content_type),
    KEY ix_marketing_generations_platform (platform),
    KEY ix_marketing_generations_status (status),
    KEY ix_marketing_generations_create_date (create_date),

    CONSTRAINT ck_marketing_generations_status
        CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 2) CMS content
CREATE TABLE cms_contents (
    content_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    user_id CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    generation_id CHAR(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    title VARCHAR(265) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    content_body LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL,

    content_type VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    platform VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    status VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT',

    create_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    update_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    PRIMARY KEY (content_id),
    KEY ix_cms_contents_user_id (user_id),
    KEY ix_cms_contents_generation_id (generation_id),
    KEY ix_cms_contents_status (status),
    KEY ix_cms_contents_content_type (content_type),
    KEY ix_cms_contents_platform (platform),
    KEY ix_cms_contents_create_date (create_date),

    CONSTRAINT ck_cms_contents_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- -----------------------------------------------------------------------------
-- DEMO DATA - 5 records for testing
-- -----------------------------------------------------------------------------

-- NOTE:
-- Only keep this block if auth_users does not already contain these IDs/emails.
-- If your project seeds auth_users elsewhere, remove the INSERT below.

INSERT INTO auth_users (
    user_id,
    email,
    password_hash,
    is_active,
    is_email_verified,
    create_date,
    update_date
) VALUES
(
    '00000000-0000-0000-0000-000000000010',
    'long01235060390@gmail.com',
    '$2a$10$sTyZZIhJzIX5Seilbe8pyO3tQr0BiTxKioYrklj2UyLOBJSiui9qG',
    1,
    1,
    '2026-01-01 09:00:00.000000',
    '2026-01-01 09:00:00.000000'
),


(
    '00000000-0000-0000-0000-000000000001',
    'demo1@yeutech.local',
    '$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOpqrstuvwxyz01',
    1,
    1,
    '2026-01-01 09:00:00.000000',
    '2026-01-01 09:00:00.000000'
),
(
    '00000000-0000-0000-0000-000000000002',
    'demo2@yeutech.local',
    '$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOpqrstuvwxyz34',
    1,
    1,
    '2026-01-01 09:05:00.000000',
    '2026-01-01 09:05:00.000000'
),
(
    '00000000-0000-0000-0000-000000000003',
    'demo3@yeutech.local',
    '$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOpqrstuvwxyz56',
    1,
    1,
    '2026-01-01 09:10:00.000000',
    '2026-01-01 09:10:00.000000'
);


INSERT INTO marketing_generations (
    generation_id,
    user_id,
    content_type,
    platform,
    target_audience,
    tone,
    goal,
    product_name,
    brand_name,
    prompt_input,
    generated_content,
    status,
    error_message,
    create_date,
    update_date
) VALUES
(
    '11111111-1111-1111-1111-111111111111',
    '00000000-0000-0000-0000-000000000010',
    'SOCIAL_POST',
    'FACEBOOK',
    'Young Adults 18-26',
    'CASUAL',
    'Increase engagement and brand awareness',
    'TechPro Headphones',
    'YeuTech',
    'Generate a fun Facebook post about our new headphones',
    '🎧 Just dropped! Our NEW TechPro Headphones are here! Crystal clear sound, 40-hour battery, and they look AMAZING. 🔥 Get yours today and join thousands of happy listeners! #YeuTech #NewRelease #AudioGear',
    'COMPLETED',
    NULL,
    '2026-01-01 10:30:00.000000',
    '2026-01-01 10:30:00.000000'
),
(
    '22222222-2222-2222-2222-222222222222',
    '00000000-0000-0000-0000-000000000010',
    'EMAIL',
    'EMAIL',
    'Existing Customers',
    'PROFESSIONAL',
    'Drive sales and customer retention',
    'Premium Smartwatch',
    'YeuTech',
    'Create an email campaign for smartwatch promotions',
    'Subject: Exclusive: 30% Off Premium Smartwatch This Week Only\n\nDear Valued Customer,\n\nWe are excited to offer you an exclusive 30% discount on our new Premium Smartwatch Series. Track your fitness, stay connected, and elevate your lifestyle.\n\nUse code: YEUTECH30\n\nShop Now: [link]',
    'COMPLETED',
    NULL,
    '2026-01-02 14:15:00.000000',
    '2026-01-02 14:15:00.000000'
),
(
    '33333333-3333-3333-3333-333333333333',
    '00000000-0000-0000-0000-000000000010',
    'BLOG_POST',
    'WEBSITE',
    'Tech Enthusiasts',
    'INFORMATIVE',
    'Establish thought leadership and SEO',
    'Smart Home Hub',
    'YeuTech',
    'Write a detailed blog post about the future of smart homes',
    'The Future of Smart Homes: What to Expect in 2026\n\nSmart home technology is evolving rapidly. From AI-powered assistants to IoT integration, we explore the latest innovations. Our Smart Home Hub brings together all your devices in one unified experience...',
    'COMPLETED',
    NULL,
    '2026-01-03 09:45:00.000000',
    '2026-01-03 09:45:00.000000'
),
(
    '44444444-4444-4444-4444-444444444444',
    '00000000-0000-0000-0000-000000000010',
    'INSTAGRAM_CAPTION',
    'INSTAGRAM',
    'Millennials 26-40',
    'TRENDY',
    'Build community and viral content',
    'Wireless Earbuds Pro',
    'YeuTech',
    'Create an Instagram caption with emojis and hashtags',
    'Your music. Your way. 🎵✨ Wireless Earbuds Pro - where premium sound meets portability. Feel the difference with our latest noise-cancelling technology. 🎧🔥 #WirelessEarbuds #AudioTech #YeuTech #MusicLovers #TechGadgets #SoundQuality',
    'COMPLETED',
    NULL,
    '2026-01-04 11:20:00.000000',
    '2026-01-04 11:20:00.000000'
),
(
    '55555555-5555-5555-5555-555555555555',
    '00000000-0000-0000-0000-000000000010',
    'PRODUCT_DESCRIPTION',
    'WEBSITE',
    'All Customers',
    'PERSUASIVE',
    'Increase product page conversions',
    'Fitness Tracker Band',
    'YeuTech',
    'Write a compelling product description for fitness tracker',
    'Fitness Tracker Band - Stay Motivated, Stay Healthy\n\nTrack every step, every heartbeat, every achievement. Our Fitness Tracker Band combines sleek design with powerful performance tracking. Monitor your daily activity, sleep quality, and health metrics in real-time.\n\nKey Features:\n- 24/7 heart rate monitoring\n- 10+ sports modes\n- 7-day battery life\n- Water-resistant up to 50m',
    'COMPLETED',
    NULL,
    '2026-01-01 15:00:00.000000',
    '2026-01-01 15:00:00.000000'
);


INSERT INTO cms_contents (
    content_id,
    user_id,
    generation_id,
    title,
    content_body,
    content_type,
    platform,
    status,
    create_date,
    update_date
) VALUES
(
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    '00000000-0000-0000-0000-000000000010',
    '11111111-1111-1111-1111-111111111111',
    'Introducing TechPro Headphones',
    '<p>We are thrilled to introduce the <strong>TechPro Headphones</strong> - engineered for audiophiles and casual listeners alike.</p><p><strong>Premium Features:</strong></p><ul><li>Crystal clear 40mm drivers</li><li>40-hour battery life</li><li>Active noise cancellation</li><li>Premium comfort design</li></ul><p>Available now at all authorized retailers!</p>',
    'SOCIAL_POST',
    'FACEBOOK',
    'PUBLISHED',
    '2026-01-01 10:35:00.000000',
    '2026-01-01 18:20:00.000000'
),
(
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
    '00000000-0000-0000-0000-000000000001',
    '22222222-2222-2222-2222-222222222222',
    'Holiday Season Special - 30% Discount',
    '<h2>Holiday Special Offer</h2><p>Celebrate the holidays with our exclusive 30% discount on all premium products!</p><p><strong>Premium Smartwatch Series</strong> - Now $349 (was $499)</p><p>Offer valid until December 31st, 2026. Use code: <code>YEUTECH30</code></p><p>Don''t miss out on the perfect gift this season!</p>',
    'EMAIL',
    'EMAIL',
    'PUBLISHED',
    '2026-01-02 14:20:00.000000',
    '2026-01-02 16:45:00.000000'
),
(
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    '00000000-0000-0000-0000-000000000010',
    '33333333-3333-3333-3333-333333333333',
    'The Evolution of Smart Home Technology',
    '<article><h2>The Evolution of Smart Home Technology in 2026</h2><p>Smart home technology has transformed from a luxury to an everyday necessity. In this comprehensive guide, we explore the latest innovations...</p><p><strong>Key Trends:</strong></p><ol><li>AI-powered home automation</li><li>Enhanced IoT device integration</li><li>Voice control improvements</li><li>Energy efficiency focus</li><li>Privacy and security advancements</li></ol><p>Our YeuTech Smart Home Hub brings all these innovations together...</p></article>',
    'BLOG_POST',
    'WEBSITE',
    'DRAFT',
    '2026-01-01 10:00:00.000000',
    '2026-01-03 14:30:00.000000'
),
(
    'dddddddd-dddd-dddd-dddd-dddddddddddd',
    '00000000-0000-0000-0000-000000000010',
    '44444444-4444-4444-4444-444444444444',
    'Wireless Earbuds Pro Launch',
    '<p>🎧✨ We''re thrilled to announce the launch of our <strong>Wireless Earbuds Pro</strong>!</p><p>Experience the perfect blend of premium sound and portability. With advanced noise-cancelling technology and seamless connectivity, your music has never sounded better.</p><p>📍 Available on Instagram Shop<br/>💳 Limited time: Free shipping with code SHIP2ME<br/>🎁 Gift packaging available</p>',
    'INSTAGRAM_CAPTION',
    'INSTAGRAM',
    'PUBLISHED',
    '2026-01-04 11:30:00.000000',
    '2026-01-04 01:15:00.000000'
),
(
    'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
    '00000000-0000-0000-0000-000000000010',
    '55555555-5555-5555-5555-555555555555',
    'Fitness Tracker Band - Product Page',
    '<section class="product-description"><h1>Fitness Tracker Band</h1><p>Stay motivated and healthy with our <strong>Fitness Tracker Band</strong> - your personal health companion.</p><h3>Key Features</h3><ul><li>24/7 heart rate monitoring with advanced sensors</li><li>10+ sports modes (running, cycling, swimming, etc.)</li><li>7-day battery life with quick charging</li><li>Water-resistant up to 50 meters</li><li>Real-time health metrics dashboard</li><li>Sleep quality tracking and analysis</li></ul><h3>Why Choose Us?</h3><p>Engineered with precision and designed for your lifestyle, the Fitness Tracker Band seamlessly integrates into your daily routine while keeping you informed about your health metrics.</p><p><strong>Price: $149.99</strong></p></section>',
    'PRODUCT_DESCRIPTION',
    'WEBSITE',
    'ARCHIVED',
    '2026-01-05 15:10:00.000000',
    '2026-01-06 09:00:00.000000'
);