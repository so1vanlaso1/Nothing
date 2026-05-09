package com.YeuTech.Infrastructure.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Entities.CmsContent;
import com.YeuTech.Domain.Repository.ICmsRepository;
import com.YeuTech.Infrastructure.Dao.CmsContentJpaDao;
import com.YeuTech.Infrastructure.Mappers.CmsMapper;

@Repository
public class CmsRepository implements ICmsRepository {

    private final CmsContentJpaDao cmsContentJpaDao;

    public CmsRepository(CmsContentJpaDao cmsContentJpaDao) {
        this.cmsContentJpaDao = cmsContentJpaDao;
    }

    @Override
    public List<CmsContent> findAll() {
        return cmsContentJpaDao.findAll().stream()
                .map(CmsMapper::toDomain)
                .toList();
    }

    @Override
    public CmsContent save(CmsContent content) {
        return CmsMapper.toDomain(cmsContentJpaDao.save(CmsMapper.toEntity(content)));
    }

    @Override
    public Optional<CmsContent> findByContentIdAndUserId(String contentId, String userId) {
        return cmsContentJpaDao.findByContentIdAndUserId(contentId, userId)
                .map(CmsMapper::toDomain);
    }

    @Override
    public List<CmsContent> findRecentByUserId(String userId, int limit) {
        return cmsContentJpaDao.findByUserIdOrderByCreateDateDesc(userId, PageRequest.of(0, limit)).stream()
                .map(CmsMapper::toDomain)
                .toList();
    }

    @Override
    public Page<CmsContent> findByUserIdWithFilters(
            String userId,
            String status,
            String contentType,
            String platform,
            Boolean hasGeneration,
            java.time.LocalDateTime fromDate,
            java.time.LocalDateTime toDate,
            String search,
            Pageable pageable) {
        return cmsContentJpaDao.findByUserIdWithFilters(
                userId,
                status,
                contentType,
                platform,
                hasGeneration,
                fromDate,
                toDate,
                search,
                pageable).map(CmsMapper::toDomain);
    }

    @Override
    public void delete(CmsContent content) {
        cmsContentJpaDao.delete(CmsMapper.toEntity(content));
    }

    @Override
    public boolean existsByContentIdAndUserId(String contentId, String userId) {
        return cmsContentJpaDao.existsByContentIdAndUserId(contentId, userId);
    }

    @Override
    public Optional<CmsContent> findByContentIdAndUserIdAndStatus(String contentId, String userId, String status) {
        return cmsContentJpaDao.findByContentIdAndUserIdAndStatus(contentId, userId, status)
                .map(CmsMapper::toDomain);
    }

    @Override
    public long countByUserIdAndCreateDateBetween(
            String userId,
            java.time.LocalDateTime startInclusive,
            java.time.LocalDateTime endExclusive) {
        return cmsContentJpaDao.countByUserIdAndCreateDateGreaterThanEqualAndCreateDateLessThan(
                userId,
                startInclusive,
                endExclusive);
    }

    @Override
    public long countByUserIdAndStatusAndCreateDateBetween(
            String userId,
            String status,
            java.time.LocalDateTime startInclusive,
            java.time.LocalDateTime endExclusive) {
        return cmsContentJpaDao.countByUserIdAndStatusAndCreateDateGreaterThanEqualAndCreateDateLessThan(
                userId,
                status,
                startInclusive,
                endExclusive);
    }
}
