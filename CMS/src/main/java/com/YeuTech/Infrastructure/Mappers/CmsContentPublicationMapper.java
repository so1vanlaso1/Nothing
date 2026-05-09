package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.CmsContentPublication;
import com.YeuTech.Infrastructure.Model.CmsContentPublicationJpaEntity;

public class CmsContentPublicationMapper {

    public static CmsContentPublication toDomain(CmsContentPublicationJpaEntity entity) {
        CmsContentPublication pub = new CmsContentPublication();
        pub.setPublicationId(entity.getPublicationId());
        pub.setContentId(entity.getContentId());
        pub.setUserId(entity.getUserId());
        pub.setDomainId(entity.getDomainId());
        pub.setSlug(entity.getSlug());
        pub.setCanonicalUrl(entity.getCanonicalUrl());
        pub.setSummary(entity.getSummary());
        pub.setCoverImageUrl(entity.getCoverImageUrl());
        pub.setPublishedDate(entity.getPublishedDate());
        pub.setActive(entity.isActive());
        return pub;
    }

    public static CmsContentPublicationJpaEntity toEntity(CmsContentPublication pub) {
        CmsContentPublicationJpaEntity entity = new CmsContentPublicationJpaEntity();
        entity.setPublicationId(pub.getPublicationId());
        entity.setContentId(pub.getContentId());
        entity.setUserId(pub.getUserId());
        entity.setDomainId(pub.getDomainId());
        entity.setSlug(pub.getSlug());
        entity.setCanonicalUrl(pub.getCanonicalUrl());
        entity.setSummary(pub.getSummary());
        entity.setCoverImageUrl(pub.getCoverImageUrl());
        entity.setPublishedDate(pub.getPublishedDate());
        entity.setActive(pub.isActive());
        return entity;
    }
}
