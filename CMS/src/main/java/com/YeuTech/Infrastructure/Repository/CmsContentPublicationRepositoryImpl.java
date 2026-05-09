package com.YeuTech.Infrastructure.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Entities.CmsContentPublication;
import com.YeuTech.Domain.Repository.ICmsContentPublicationRepository;
import com.YeuTech.Infrastructure.Dao.CmsContentPublicationJpaDao;
import com.YeuTech.Infrastructure.Mappers.CmsContentPublicationMapper;

@Repository
public class CmsContentPublicationRepositoryImpl implements ICmsContentPublicationRepository {

    private final CmsContentPublicationJpaDao publicationJpaDao;

    public CmsContentPublicationRepositoryImpl(CmsContentPublicationJpaDao publicationJpaDao) {
        this.publicationJpaDao = publicationJpaDao;
    }

    @Override
    public CmsContentPublication save(CmsContentPublication publication) {
        return CmsContentPublicationMapper.toDomain(
                publicationJpaDao.save(CmsContentPublicationMapper.toEntity(publication)));
    }

    @Override
    public Optional<CmsContentPublication> findByDomainIdAndContentId(String domainId, String contentId) {
        return publicationJpaDao.findByDomainIdAndContentId(domainId, contentId)
                .map(CmsContentPublicationMapper::toDomain);
    }

    @Override
    public Optional<CmsContentPublication> findByDomainIdAndContentIdAndIsActive(
            String domainId, String contentId, boolean isActive) {
        return publicationJpaDao.findByDomainIdAndContentIdAndIsActive(domainId, contentId, isActive)
                .map(CmsContentPublicationMapper::toDomain);
    }

    @Override
    public Optional<CmsContentPublication> findByContentId(String contentId) {
        return publicationJpaDao.findByContentId(contentId)
                .map(CmsContentPublicationMapper::toDomain);
    }

    @Override
    public List<CmsContentPublication> findByUserId(String userId) {
        return publicationJpaDao.findByUserIdOrderByPublishedDateDesc(userId).stream()
                .map(CmsContentPublicationMapper::toDomain)
                .toList();
    }
}
