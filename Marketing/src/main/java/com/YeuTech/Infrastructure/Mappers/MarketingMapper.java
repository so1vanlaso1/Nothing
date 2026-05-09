package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.MarketingGeneration;
import com.YeuTech.Infrastructure.Model.MarketingGenerationJpaEntity;

public class MarketingMapper {
    public static MarketingGeneration toDomain(MarketingGenerationJpaEntity entity) {
        MarketingGeneration generation = new MarketingGeneration();
        generation.setGenerationId(entity.getGenerationId());
        generation.setUserId(entity.getUserId());
        generation.setContentType(entity.getContentType());
        generation.setPlatform(entity.getPlatform());
        generation.setTarget_audience(entity.getTarget_audience());
        generation.setTone(entity.getTone());
        generation.setGoal(entity.getGoal());
        generation.setProduct_name(entity.getProduct_name());
        generation.setBrand_name(entity.getBrand_name());
        generation.setPrompt_input(entity.getPrompt_input());
        generation.setGenerated_content(entity.getGenerated_content());
        generation.setStatus(entity.getStatus());
        generation.setError_message(entity.getError_message());
        generation.setCreateDate(entity.getCreateDate());
        generation.setUpdate_date(entity.getUpdate_date());
        return generation;
    }

    public static MarketingGenerationJpaEntity toEntity(MarketingGeneration generation) {
        MarketingGenerationJpaEntity entity = new MarketingGenerationJpaEntity();
        entity.setGenerationId(generation.getGenerationId());
        entity.setUserId(generation.getUserId());
        entity.setContentType(generation.getContentType());
        entity.setPlatform(generation.getPlatform());
        entity.setTarget_audience(generation.getTarget_audience());
        entity.setTone(generation.getTone());
        entity.setGoal(generation.getGoal());
        entity.setProduct_name(generation.getProduct_name());
        entity.setBrand_name(generation.getBrand_name());
        entity.setPrompt_input(generation.getPrompt_input());
        entity.setGenerated_content(generation.getGenerated_content());
        entity.setStatus(generation.getStatus());
        entity.setError_message(generation.getError_message());
        entity.setCreateDate(generation.getCreateDate());
        entity.setUpdate_date(generation.getUpdate_date());
        return entity;
    }
}
