-- -----------------------------------------------------------------------------
-- Migration: V5__add_marketing_config_to_user_profiles.sql
-- Description: Add marketing automation fields like domain and industry to user_profiles
-- -----------------------------------------------------------------------------

ALTER TABLE user_profiles
ADD COLUMN config JSON DEFAULT NULL;
