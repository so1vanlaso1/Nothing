package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.EmailOtp;
import com.YeuTech.Infrastructure.Model.EmailOtpJpaEntity;

public class EmailOtpMapper {
    public static EmailOtpJpaEntity toEntity(EmailOtp domain) {
        EmailOtpJpaEntity entity = new EmailOtpJpaEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setOtpCode(domain.getOtpCode());
        entity.setUsed(domain.isUsed());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUsedAt(domain.getUsedAt());
        entity.setCreateDate(domain.getCreatedDate());
        return entity;
    }

    public static EmailOtp toDomain(EmailOtpJpaEntity entity) {
        EmailOtp domain = new EmailOtp();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setOtpCode(entity.getOtpCode());
        domain.setUsed(entity.isUsed());
        domain.setExpiresAt(entity.getExpiresAt());
        domain.setUsedAt(entity.getUsedAt());
        domain.setCreatedDate(entity.getCreateDate());
        return domain;
    }
}
