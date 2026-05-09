package com.YeuTech.Infrastructure.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Entities.CmsMarketingGeneration;
import com.YeuTech.Domain.Repository.ICmsMarketingGenerationRepository;
import com.YeuTech.Infrastructure.Dao.CmsMarketingGenerationJpaDao;
import com.YeuTech.Infrastructure.Mappers.CmsMarketingGenerationMapper;

@Repository
public class CmsMarketingGenerationRepository implements ICmsMarketingGenerationRepository {
    private final CmsMarketingGenerationJpaDao cmsMarketingGenerationJpaDao;

    public CmsMarketingGenerationRepository(CmsMarketingGenerationJpaDao cmsMarketingGenerationJpaDao) {
        this.cmsMarketingGenerationJpaDao = cmsMarketingGenerationJpaDao;
    }

    @Override
    public Optional<CmsMarketingGeneration> findByGenerationIdAndUserId(String generationId, String userId) {
        return cmsMarketingGenerationJpaDao.findByGenerationIdAndUserId(generationId, userId)
                .map(CmsMarketingGenerationMapper::toDomain);
    }

    @Override
    public Optional<CmsMarketingGeneration> findByGenerationId(String generationId) {
        return cmsMarketingGenerationJpaDao.findById(generationId)
                .map(CmsMarketingGenerationMapper::toDomain);
    }

    @Override
    public List<CmsMarketingGeneration> findRecentByUserId(String userId, int limit) {
        return cmsMarketingGenerationJpaDao.findByUserIdOrderByCreateDateDesc(userId, PageRequest.of(0, limit)).stream()
                .map(CmsMarketingGenerationMapper::toDomain)
                .toList();
    }

    @Override
    public long countByUserIdAndCreateDateBetween(String userId, LocalDateTime startInclusive, LocalDateTime endExclusive) {
        return cmsMarketingGenerationJpaDao.countByUserIdAndCreateDateGreaterThanEqualAndCreateDateLessThan(
                userId,
                startInclusive,
                endExclusive);
    }
}
