package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.RefreshToken;
import com.YeuTech.Infrastructure.Model.RefreshTokenJpaEntity;

public class RefreshTokenMapper {
    public static RefreshTokenJpaEntity toEntity(RefreshToken domain) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setTokenHash(domain.getTokenHash());
        entity.setJti(domain.getJti());
        entity.setIssuedAt(domain.getIssuedAt());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setRevoked(domain.isRevoked());
        entity.setRevokedAt(domain.getRevokedAt());
        entity.setCreateDate(domain.getCreateDate());
        return entity;
    }

    public static RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        RefreshToken domain = new RefreshToken();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setTokenHash(entity.getTokenHash());
        domain.setJti(entity.getJti());
        domain.setIssuedAt(entity.getIssuedAt());
        domain.setExpiresAt(entity.getExpiresAt());
        domain.setRevoked(entity.isRevoked());
        domain.setRevokedAt(entity.getRevokedAt());
        domain.setCreateDate(entity.getCreateDate());
        return domain;
    }
}
