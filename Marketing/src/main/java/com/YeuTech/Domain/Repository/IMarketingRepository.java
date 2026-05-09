package com.YeuTech.Domain.Repository;

import java.util.List;
import java.util.Optional;

import com.YeuTech.Domain.Entities.MarketingGeneration;

public interface IMarketingRepository {
    List<MarketingGeneration> findAll();

    MarketingGeneration save(MarketingGeneration generation);

    boolean existsByGenerationIdAndUserId(String generationId, String userId);

    List<MarketingGeneration> findByUserId(String userId);

    Optional<MarketingGeneration> findByGenerationIdAndUserId(String generationId, String userId);

    List<MarketingGeneration> findRecentByUserId(String userId, int limit);
}
