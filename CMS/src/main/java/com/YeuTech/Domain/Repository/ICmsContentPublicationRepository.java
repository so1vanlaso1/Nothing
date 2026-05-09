package com.YeuTech.Domain.Repository;

import java.util.List;
import java.util.Optional;

import com.YeuTech.Domain.Entities.CmsContentPublication;

public interface ICmsContentPublicationRepository {
    CmsContentPublication save(CmsContentPublication publication);

    Optional<CmsContentPublication> findByDomainIdAndContentId(String domainId, String contentId);

    Optional<CmsContentPublication> findByDomainIdAndContentIdAndIsActive(String domainId, String contentId, boolean isActive);

    Optional<CmsContentPublication> findByContentId(String contentId);

    List<CmsContentPublication> findByUserId(String userId);
}
