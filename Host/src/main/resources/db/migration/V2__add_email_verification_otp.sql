CREATE TABLE auth_email_otps (
  otp_id        CHAR(36)     NOT NULL,
  user_id       CHAR(36)     NOT NULL,
  otp_code      CHAR(6)      NOT NULL,
  is_used       BOOLEAN      NOT NULL DEFAULT FALSE,
  expires_at    DATETIME(6)  NOT NULL,
  used_at       DATETIME(6)  DEFAULT NULL,
  create_date   DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (otp_id),
  KEY ix_email_otps_user_id (user_id),
  KEY ix_email_otps_expires (expires_at),
  CONSTRAINT fk_email_otps_user
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;