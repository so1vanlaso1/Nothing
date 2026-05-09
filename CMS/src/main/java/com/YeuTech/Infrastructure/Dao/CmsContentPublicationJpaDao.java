package com.YeuTech.Infrastructure.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.CmsContentPublicationJpaEntity;

@Repository
public interface CmsContentPublicationJpaDao extends JpaRepository<CmsContentPublicationJpaEntity, String> {

    Optional<CmsContentPublicationJpaEntity> findByDomainIdAndContentId(String domainId, String contentId);

    Optional<CmsContentPublicationJpaEntity> findByDomainIdAndContentIdAndIsActive(String domainId, String contentId, boolean isActive);

    Optional<CmsContentPublicationJpaEntity> findByContentId(String contentId);

    List<CmsContentPublicationJpaEntity> findByUserIdOrderByPublishedDateDesc(String userId);
}
