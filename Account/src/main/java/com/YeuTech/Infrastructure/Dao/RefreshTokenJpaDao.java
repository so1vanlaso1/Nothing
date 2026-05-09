package com.YeuTech.Infrastructure.Dao;

import com.YeuTech.Infrastructure.Model.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenJpaDao extends JpaRepository<RefreshTokenJpaEntity, String> {
//     RefreshTokenJpaEntity findByJti(String jti);
     void deleteByUserId(String userId);
     Optional<RefreshTokenJpaEntity> findByUserIdAndTokenHash(String userId, String tokenHash);
}
