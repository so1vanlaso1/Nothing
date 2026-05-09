package com.YeuTech.Application.Mappers;

import com.YeuTech.Domain.Entities.CmsContent;
import com.YeuTech.Dtos.Response.CmsContentResponseDto;

public class CmsDtoMapper {
    public static CmsContentResponseDto toResponseDto(CmsContent content) {
        if (content == null)
            return null;

        return new CmsContentResponseDto(
                content.getContentId(),
                content.getUserId(),
                content.getGenerationId(),
                content.getTitle(),
                content.getContentBody(),
                content.getContentType(),
                content.getPlatform(),
                content.getStatus(),
                content.getCreateDate(),
                content.getUpdateDate());
    }
}
