package com.YeuTech.Infrastructure.Dao;

import com.YeuTech.Infrastructure.Model.EmailOtpJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOtpJpaDao extends JpaRepository<EmailOtpJpaEntity, String> {
    Optional<EmailOtpJpaEntity> findByUserIdAndIsUsedFalse(String userId);
    void deleteByUserId(String userId);
}
