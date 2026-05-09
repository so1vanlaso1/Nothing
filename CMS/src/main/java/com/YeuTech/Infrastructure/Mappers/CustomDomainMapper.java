package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.CustomDomain;
import com.YeuTech.Infrastructure.Model.CustomDomainJpaEntity;

public class CustomDomainMapper {

    public static CustomDomain toDomain(CustomDomainJpaEntity entity) {
        CustomDomain domain = new CustomDomain();
        domain.setDomainId(entity.getDomainId());
        domain.setUserId(entity.getUserId());
        domain.setDomainName(entity.getDomainName());
        domain.setVerificationToken(entity.getVerificationToken());
        domain.setVerificationStatus(entity.getVerificationStatus());
        domain.setDnsStatus(entity.getDnsStatus());
        domain.setSslStatus(entity.getSslStatus());
        domain.setActive(entity.isActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());
        return domain;
    }

    public static CustomDomainJpaEntity toEntity(CustomDomain domain) {
        CustomDomainJpaEntity entity = new CustomDomainJpaEntity();
        entity.setDomainId(domain.getDomainId());
        entity.setUserId(domain.getUserId());
        entity.setDomainName(domain.getDomainName());
        entity.setVerificationToken(domain.getVerificationToken());
        entity.setVerificationStatus(domain.getVerificationStatus());
        entity.setDnsStatus(domain.getDnsStatus());
        entity.setSslStatus(domain.getSslStatus());
        entity.setActive(domain.isActive());
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setUpdatedDate(domain.getUpdatedDate());
        return entity;
    }
}
