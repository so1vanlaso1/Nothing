package com.YeuTech.Infrastructure.Dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.YeuTech.Infrastructure.Model.SocialMediaConnectionJpaEntity;

public interface SocialMediaConnectionJpaDao extends JpaRepository<SocialMediaConnectionJpaEntity, String> {
    Optional<SocialMediaConnectionJpaEntity> findByUserIdAndPlatform(String userId, String platform);
}
