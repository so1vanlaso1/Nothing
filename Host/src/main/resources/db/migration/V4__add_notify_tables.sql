-- -----------------------------------------------------------------------------
-- Migration: V4__add_notifications_table.sql
-- Description: Notifications for AI generation, content workflow, and delivery
-- -----------------------------------------------------------------------------

CREATE TABLE notifications (
    notification_id    CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    user_id            CHAR(36) COLLATE utf8mb4_unicode_ci NOT NULL,

    -- Optional references to existing business records
    generation_id      CHAR(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    content_id         CHAR(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    -- Where the notification is delivered
    channel            VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL,   -- EMAIL | IN_APP

    -- What happened in the system
    event_type         VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,   -- GENERATION_COMPLETED, GENERATION_FAILED, CONTENT_PUBLISHED, POST_FAILED, etc.

    title              VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    message            TEXT COLLATE utf8mb4_unicode_ci NOT NULL,

    -- For EMAIL notifications
    recipient_email    VARCHAR(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    -- Optional UI/navigation payload
    action_url         VARCHAR(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    metadata_json      JSON DEFAULT NULL,

    -- Sending state
    delivery_status    VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING', -- PENDING | SENT | FAILED
    sent_at            DATETIME(6) DEFAULT NULL,
    error_message      VARCHAR(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

    -- User read state
    read_at            DATETIME(6) DEFAULT NULL,

    create_date        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    update_date        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    PRIMARY KEY (notification_id),

    KEY ix_notifications_user_id (user_id),
    KEY ix_notifications_user_create_date (user_id, create_date),
    KEY ix_notifications_user_read_date (user_id, read_at, create_date),
    KEY ix_notifications_event_type (event_type),
    KEY ix_notifications_delivery_status (delivery_status),
    KEY ix_notifications_generation_id (generation_id),
    KEY ix_notifications_content_id (content_id),
    KEY ix_notifications_create_date (create_date),

    CONSTRAINT ck_notifications_channel
        CHECK (channel IN ('EMAIL', 'IN_APP')),

    CONSTRAINT ck_notifications_delivery_status
        CHECK (delivery_status IN ('PENDING', 'SENT', 'FAILED')),

    CONSTRAINT ck_notifications_email_recipient
        CHECK (
            (channel = 'EMAIL' AND recipient_email IS NOT NULL)
            OR
            (channel = 'IN_APP')
        )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- DEMO DATA - 10 unread notifications for test user
-- -----------------------------------------------------------------------------
INSERT INTO notifications (
    notification_id,
    user_id,
    generation_id,
    content_id,
    channel,
    event_type,
    title,
    message,
    recipient_email,
    action_url,
    metadata_json,
    delivery_status,
    sent_at,
    error_message,
    read_at,
    create_date,
    update_date
) VALUES
(
    '90000000-0000-0000-0000-000000000001',
    '00000000-0000-0000-0000-000000000010',
    '11111111-1111-1111-1111-111111111111',
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    'IN_APP',
    'GENERATION_COMPLETED',
    'Generation Completed',
    'Your Facebook post content has been generated successfully.',
    NULL,
    '/marketing/generations/11111111-1111-1111-1111-111111111111',
    JSON_OBJECT('priority', 'normal', 'source', 'marketing-service'),
    'SENT',
    '2026-02-02 09:10:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:10:00.000000',
    '2026-02-02 09:10:00.000000'
),
(
    '90000000-0000-0000-0000-000000000002',
    '00000000-0000-0000-0000-000000000010',
    '22222222-2222-2222-2222-222222222222',
    NULL,
    'IN_APP',
    'GENERATION_COMPLETED',
    'Email Campaign Ready',
    'Your email campaign draft is ready for review.',
    NULL,
    '/marketing/generations/22222222-2222-2222-2222-222222222222',
    JSON_OBJECT('priority', 'normal', 'source', 'marketing-service'),
    'SENT',
    '2026-02-02 09:15:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:15:00.000000',
    '2026-02-02 09:15:00.000000'
),
(
    '90000000-0000-0000-0000-000000000003',
    '00000000-0000-0000-0000-000000000010',
    '33333333-3333-3333-3333-333333333333',
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    'IN_APP',
    'CONTENT_DRAFT_SAVED',
    'Draft Saved',
    'Your blog draft was saved to CMS.',
    NULL,
    '/cms/contents/cccccccc-cccc-cccc-cccc-cccccccccccc',
    JSON_OBJECT('priority', 'low', 'source', 'cms-service'),
    'SENT',
    '2026-02-02 09:20:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:20:00.000000',
    '2026-02-02 09:20:00.000000'
),
(
    '90000000-0000-0000-0000-000000000004',
    '00000000-0000-0000-0000-000000000010',
    '44444444-4444-4444-4444-444444444444',
    'dddddddd-dddd-dddd-dddd-dddddddddddd',
    'IN_APP',
    'CONTENT_PUBLISHED',
    'Content Published',
    'Your Instagram caption has been published.',
    NULL,
    '/cms/contents/dddddddd-dddd-dddd-dddd-dddddddddddd',
    JSON_OBJECT('priority', 'high', 'source', 'cms-service'),
    'SENT',
    '2026-02-02 09:25:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:25:00.000000',
    '2026-02-02 09:25:00.000000'
),
(
    '90000000-0000-0000-0000-000000000005',
    '00000000-0000-0000-0000-000000000010',
    '55555555-5555-5555-5555-555555555555',
    'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
    'IN_APP',
    'CONTENT_ARCHIVED',
    'Content Archived',
    'The product description content was moved to archive.',
    NULL,
    '/cms/contents/eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
    JSON_OBJECT('priority', 'low', 'source', 'cms-service'),
    'SENT',
    '2026-02-02 09:30:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:30:00.000000',
    '2026-02-02 09:30:00.000000'
),
(
    '90000000-0000-0000-0000-000000000006',
    '00000000-0000-0000-0000-000000000010',
    NULL,
    NULL,
    'IN_APP',
    'SYSTEM_INFO',
    'Profile Reminder',
    'Please complete your profile to improve AI output quality.',
    NULL,
    '/account/profile',
    JSON_OBJECT('priority', 'normal', 'source', 'account-service'),
    'SENT',
    '2026-02-02 09:35:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:35:00.000000',
    '2026-02-02 09:35:00.000000'
),
(
    '90000000-0000-0000-0000-000000000007',
    '00000000-0000-0000-0000-000000000010',
    NULL,
    NULL,
    'IN_APP',
    'SYSTEM_WARNING',
    'Action Required',
    'One of your draft posts is missing required fields.',
    NULL,
    '/cms/contents',
    JSON_OBJECT('priority', 'high', 'source', 'cms-service'),
    'SENT',
    '2026-02-02 09:40:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:40:00.000000',
    '2026-02-02 09:40:00.000000'
),
(
    '90000000-0000-0000-0000-000000000008',
    '00000000-0000-0000-0000-000000000010',
    NULL,
    NULL,
    'IN_APP',
    'DELIVERY_RETRY',
    'Retry Scheduled',
    'A failed post delivery has been scheduled for retry.',
    NULL,
    '/notify/history',
    JSON_OBJECT('priority', 'normal', 'source', 'notify-service'),
    'SENT',
    '2026-02-02 09:45:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:45:00.000000',
    '2026-02-02 09:45:00.000000'
),
(
    '90000000-0000-0000-0000-000000000009',
    '00000000-0000-0000-0000-000000000010',
    NULL,
    NULL,
    'IN_APP',
    'SECURITY_ALERT',
    'New Login Detected',
    'A new sign-in was detected on your account.',
    NULL,
    '/account/security',
    JSON_OBJECT('priority', 'high', 'source', 'auth-service'),
    'SENT',
    '2026-02-02 09:50:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:50:00.000000',
    '2026-02-02 09:50:00.000000'
),
(
    '90000000-0000-0000-0000-000000000010',
    '00000000-0000-0000-0000-000000000010',
    NULL,
    NULL,
    'IN_APP',
    'DAILY_SUMMARY',
    'Daily Summary Ready',
    'Your daily marketing activity summary is available.',
    NULL,
    '/notify/daily-summary',
    JSON_OBJECT('priority', 'low', 'source', 'notify-service'),
    'SENT',
    '2026-02-02 09:55:00.000000',
    NULL,
    NULL,
    '2026-02-02 09:55:00.000000',
    '2026-02-02 09:55:00.000000'
);