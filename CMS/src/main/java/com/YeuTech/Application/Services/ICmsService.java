package com.YeuTech.Application.Services;

import java.util.List;

import com.YeuTech.Dtos.Request.CmsContentQueryRequestDto;
import com.YeuTech.Dtos.Request.CreateCmsContentFromGenerationRequestDto;
import com.YeuTech.Dtos.Request.CreateCmsContentRequestDto;
import com.YeuTech.Dtos.Request.UpdateCmsContentRequestDto;
import com.YeuTech.Dtos.Response.CmsContentDetailResponseDto;
import com.YeuTech.Dtos.Response.CmsContentListResponseDto;
import com.YeuTech.Dtos.Response.CmsContentResponseDto;
import com.YeuTech.Dtos.Response.CmsDashboardRecentItemsResponseDto;
import com.YeuTech.Dtos.Response.CmsDashboardSummaryResponseDto;
import com.YeuTech.Dtos.Response.CmsStatusChangeResponseDto;

public interface ICmsService {
    List<CmsContentResponseDto> getAllCms();

    CmsContentResponseDto createContent(String currentUserEmail, CreateCmsContentRequestDto request);

    CmsContentResponseDto createContentFromGeneration(
            String currentUserEmail,
            String generationId,
            CreateCmsContentFromGenerationRequestDto request);

    CmsContentListResponseDto listContents(String currentUserEmail, CmsContentQueryRequestDto request);

    CmsDashboardRecentItemsResponseDto getDashboardRecentItems(String currentUserEmail, Integer limit);

    CmsDashboardSummaryResponseDto getDashboardSummary(String currentUserEmail, String period);

    CmsContentDetailResponseDto getContentById(String currentUserEmail, String contentId);

    String getContentHtmlById(String currentUserEmail, String contentId);

    CmsContentResponseDto updateContent(String currentUserEmail, String contentId, UpdateCmsContentRequestDto request);

    CmsStatusChangeResponseDto publishContent(String currentUserEmail, String contentId);

    CmsStatusChangeResponseDto moveContentToDraft(String currentUserEmail, String contentId);

    CmsStatusChangeResponseDto archiveContent(String currentUserEmail, String contentId);

    void deleteContent(String currentUserEmail, String contentId);
}
