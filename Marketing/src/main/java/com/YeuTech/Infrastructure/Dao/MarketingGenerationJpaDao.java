package com.YeuTech.Infrastructure.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.MarketingGenerationJpaEntity;

@Repository
public interface MarketingGenerationJpaDao extends JpaRepository<MarketingGenerationJpaEntity, String> {
    boolean existsByGenerationIdAndUserId(String generationId, String userId);
    List<MarketingGenerationJpaEntity> findByUserId(String userId);
    Optional<MarketingGenerationJpaEntity> findByGenerationIdAndUserId(String generationId, String userId);
    List<MarketingGenerationJpaEntity> findByUserIdOrderByCreateDateDesc(String userId, Pageable pageable);
}
