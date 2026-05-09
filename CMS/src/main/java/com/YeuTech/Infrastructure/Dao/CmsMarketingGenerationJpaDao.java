package com.YeuTech.Infrastructure.Dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.CmsMarketingGenerationJpaEntity;

@Repository
public interface CmsMarketingGenerationJpaDao extends JpaRepository<CmsMarketingGenerationJpaEntity, String> {
    Optional<CmsMarketingGenerationJpaEntity> findByGenerationIdAndUserId(String generationId, String userId);

    List<CmsMarketingGenerationJpaEntity> findByUserIdOrderByCreateDateDesc(String userId, org.springframework.data.domain.Pageable pageable);

    long countByUserIdAndCreateDateGreaterThanEqualAndCreateDateLessThan(
            String userId,
            LocalDateTime startInclusive,
            LocalDateTime endExclusive);
}
