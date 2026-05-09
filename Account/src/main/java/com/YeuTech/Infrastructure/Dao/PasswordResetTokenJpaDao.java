package com.YeuTech.Infrastructure.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.PasswordResetTokenJpaEntity;

@Repository
public interface PasswordResetTokenJpaDao extends JpaRepository<PasswordResetTokenJpaEntity, String> {
    Optional<PasswordResetTokenJpaEntity> findByTokenHashAndUsedFalse(String tokenHash);
    Optional<PasswordResetTokenJpaEntity> findByUserIdAndTokenHash(String userId, String tokenHash);
}