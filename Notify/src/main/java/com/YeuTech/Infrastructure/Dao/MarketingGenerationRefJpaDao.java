package com.YeuTech.Infrastructure.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.MarketingGenerationJpaEntity;

@Repository
public interface MarketingGenerationRefJpaDao extends JpaRepository<MarketingGenerationJpaEntity, String> {
    boolean existsByGenerationIdAndUserId(String generationId, String userId);
}
