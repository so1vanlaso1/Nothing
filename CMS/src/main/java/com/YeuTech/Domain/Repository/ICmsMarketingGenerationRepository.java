package com.YeuTech.Domain.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.YeuTech.Domain.Entities.CmsMarketingGeneration;

public interface ICmsMarketingGenerationRepository {
    Optional<CmsMarketingGeneration> findByGenerationIdAndUserId(String generationId, String userId);

    Optional<CmsMarketingGeneration> findByGenerationId(String generationId);

    List<CmsMarketingGeneration> findRecentByUserId(String userId, int limit);

    long countByUserIdAndCreateDateBetween(String userId, LocalDateTime startInclusive, LocalDateTime endExclusive);
}
