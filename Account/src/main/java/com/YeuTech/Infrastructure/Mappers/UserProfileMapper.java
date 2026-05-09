package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.UserProfile;
import com.YeuTech.Infrastructure.Model.UserProfileJpaEntity;

public class UserProfileMapper {
    public static UserProfile toDomain(UserProfileJpaEntity entity) {
        if (entity == null) return null;
        UserProfile domain = new UserProfile();
        domain.setUserId(entity.getUserId());
        domain.setFirstName(entity.getFirstName());
        domain.setLastName(entity.getLastName());
        domain.setAvatarUrl(entity.getAvatarUrl());
        domain.setConfig(entity.getConfig());
        return domain;
    }

    public static UserProfileJpaEntity toEntity(UserProfile domain) {
        if (domain == null) return null;
        UserProfileJpaEntity entity = new UserProfileJpaEntity();
        entity.setUserId(domain.getUserId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setAvatarUrl(domain.getAvatarUrl());
        entity.setConfig(domain.getConfig());
        return entity;
    }
}
