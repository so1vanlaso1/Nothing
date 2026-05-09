package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.SocialMediaConnection;
import com.YeuTech.Domain.Enums.SocialPlatform;
import com.YeuTech.Infrastructure.Model.SocialMediaConnectionJpaEntity;

public class SocialMediaConnectionMapper {
    public static SocialMediaConnection toDomain(SocialMediaConnectionJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        SocialMediaConnection domain = new SocialMediaConnection();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setPlatform(SocialPlatform.valueOf(entity.getPlatform()));
        domain.setAccessToken(entity.getAccessToken());
        domain.setLongLivedUserToken(entity.getLongLivedUserToken());
        domain.setPageId(entity.getPageId());
        domain.setTokenExpiryDate(entity.getTokenExpiryDate());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());
        return domain;
    }

    public static SocialMediaConnectionJpaEntity toEntity(SocialMediaConnection domain) {
        if (domain == null) {
            return null;
        }
        SocialMediaConnectionJpaEntity entity = new SocialMediaConnectionJpaEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setPlatform(domain.getPlatform().name());
        entity.setAccessToken(domain.getAccessToken());
        entity.setLongLivedUserToken(domain.getLongLivedUserToken());
        entity.setPageId(domain.getPageId());
        entity.setTokenExpiryDate(domain.getTokenExpiryDate());
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setUpdatedDate(domain.getUpdatedDate());
        return entity;
    }
}
