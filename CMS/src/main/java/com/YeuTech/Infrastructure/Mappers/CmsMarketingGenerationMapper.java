package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.CmsMarketingGeneration;
import com.YeuTech.Infrastructure.Model.CmsMarketingGenerationJpaEntity;

public class CmsMarketingGenerationMapper {
    private CmsMarketingGenerationMapper() {
    }

    public static CmsMarketingGeneration toDomain(CmsMarketingGenerationJpaEntity entity) {
        CmsMarketingGeneration generation = new CmsMarketingGeneration();
        generation.setGenerationId(entity.getGenerationId());
        generation.setUserId(entity.getUserId());
        generation.setContentType(entity.getContentType());
        generation.setPlatform(entity.getPlatform());
        generation.setTargetAudience(entity.getTargetAudience());
        generation.setTone(entity.getTone());
        generation.setGoal(entity.getGoal());
        generation.setProductName(entity.getProductName());
        generation.setBrandName(entity.getBrandName());
        generation.setPromptInput(entity.getPromptInput());
        generation.setGeneratedContent(entity.getGeneratedContent());
        generation.setStatus(entity.getStatus());
        generation.setErrorMessage(entity.getErrorMessage());
        generation.setCreateDate(entity.getCreateDate());
        generation.setUpdateDate(entity.getUpdateDate());
        return generation;
    }
}
