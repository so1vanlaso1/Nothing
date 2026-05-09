package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.CmsContent;
import com.YeuTech.Infrastructure.Model.CmsContentJpaEntity;

public class CmsMapper {
    public static CmsContent toDomain(CmsContentJpaEntity entity) {
        CmsContent content = new CmsContent();
        content.setContentId(entity.getContentId());
        content.setUserId(entity.getUserId());
        content.setGenerationId(entity.getGenerationId());
        content.setTitle(entity.getTitle());
        content.setContentBody(entity.getContentBody());
        content.setContentType(entity.getContentType());
        content.setPlatform(entity.getPlatform());
        content.setStatus(entity.getStatus());
        content.setCreateDate(entity.getCreateDate());
        content.setUpdateDate(entity.getUpdateDate());
        return content;
    }

    public static CmsContentJpaEntity toEntity(CmsContent content) {
        CmsContentJpaEntity entity = new CmsContentJpaEntity();
        entity.setContentId(content.getContentId());
        entity.setUserId(content.getUserId());
        entity.setGenerationId(content.getGenerationId());
        entity.setTitle(content.getTitle());
        entity.setContentBody(content.getContentBody());
        entity.setContentType(content.getContentType());
        entity.setPlatform(content.getPlatform());
        entity.setStatus(content.getStatus());
        entity.setCreateDate(content.getCreateDate());
        entity.setUpdateDate(content.getUpdateDate());
        return entity;
    }
}
