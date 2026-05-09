package com.YeuTech.Infrastructure.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Entities.MarketingGeneration;
import com.YeuTech.Domain.Repository.IMarketingRepository;
import com.YeuTech.Infrastructure.Dao.MarketingGenerationJpaDao;
import com.YeuTech.Infrastructure.Mappers.MarketingMapper;

@Repository
public class MarketingRepository implements IMarketingRepository {

    private final MarketingGenerationJpaDao marketingGenerationJpaDao;

    public MarketingRepository(MarketingGenerationJpaDao marketingGenerationJpaDao) {
        this.marketingGenerationJpaDao = marketingGenerationJpaDao;
    }

    @Override
    public List<MarketingGeneration> findAll() {
        return marketingGenerationJpaDao.findAll().stream()
                .map(MarketingMapper::toDomain)
                .toList();
    }

    @Override
    public MarketingGeneration save(MarketingGeneration generation) {
        return MarketingMapper.toDomain(marketingGenerationJpaDao.save(MarketingMapper.toEntity(generation)));
    }

    @Override
    public boolean existsByGenerationIdAndUserId(String generationId, String userId) {
        return marketingGenerationJpaDao.existsByGenerationIdAndUserId(generationId, userId);
    }

    @Override
    public List<MarketingGeneration> findByUserId(String userId) {
        return marketingGenerationJpaDao.findByUserId(userId).stream()
                .map(MarketingMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<MarketingGeneration> findByGenerationIdAndUserId(String generationId, String userId) {
        return marketingGenerationJpaDao.findByGenerationIdAndUserId(generationId, userId)
                .map(MarketingMapper::toDomain);
    }

    @Override
    public List<MarketingGeneration> findRecentByUserId(String userId, int limit) {
        return marketingGenerationJpaDao.findByUserIdOrderByCreateDateDesc(userId, PageRequest.of(0, limit)).stream()
                .map(MarketingMapper::toDomain)
                .toList();
    }
}
