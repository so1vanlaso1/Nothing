package com.YeuTech.Api.Controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.ICmsService;
import com.YeuTech.Dtos.ApiResponseFormat;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/cms")
@Tag(name = "CMS", description = "CMS APIs")
@SecurityRequirement(name = "bearerAuth")
public class CMSController {

    private final ICmsService cmsService;

    public CMSController(ICmsService cmsService) {
        this.cmsService = cmsService;
    }

    @GetMapping("/status")
    @Operation(summary = "CMS status", description = "Simple endpoint to verify CMS API availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CMS API is available")
    })
    public String status() {
        return "OK";
    }

    @PostMapping("/contents")
    @Operation(summary = "Create CMS content manually", description = "Create a CMS content item without generation source")
    public ResponseEntity<ApiResponseFormat<CmsContentResponseDto>> createContent(
            Authentication authentication,
            @RequestBody CreateCmsContentRequestDto request) {
        CmsContentResponseDto dto = cmsService.createContent(authentication.getName(), request);
        ApiResponseFormat<CmsContentResponseDto> response = new ApiResponseFormat<>(
                201,
                "cms content created successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/contents/from-generation/{generationId}")
    @Operation(
            summary = "Create CMS content from generation",
            description = "Create a CMS content item from an existing marketing generation")
    public ResponseEntity<ApiResponseFormat<CmsContentResponseDto>> createContentFromGeneration(
            Authentication authentication,
            @PathVariable("generationId") String generationId,
            @RequestBody(required = false) CreateCmsContentFromGenerationRequestDto request) {
        CmsContentResponseDto dto = cmsService.createContentFromGeneration(authentication.getName(), generationId, request);
        ApiResponseFormat<CmsContentResponseDto> response = new ApiResponseFormat<>(
                201,
                "cms content created successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/contents")
    @Operation(summary = "List CMS contents", description = "List CMS contents with filters/search/sort/pagination")
    public ResponseEntity<ApiResponseFormat<CmsContentListResponseDto>> listContents(
            Authentication authentication,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "contentType", required = false) String contentType,
            @RequestParam(name = "platform", required = false) String platform,
            @RequestParam(name = "hasGeneration", required = false) Boolean hasGeneration,
            @RequestParam(name = "fromDate", required = false) String fromDate,
            @RequestParam(name = "toDate", required = false) String toDate,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortOrder", required = false) String sortOrder) {
        CmsContentQueryRequestDto request = new CmsContentQueryRequestDto();
        request.setStatus(status);
        request.setContentType(contentType);
        request.setPlatform(platform);
        request.setHasGeneration(hasGeneration);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        request.setSearch(search);
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setSortBy(sortBy);
        request.setSortOrder(sortOrder);

        CmsContentListResponseDto dto = cmsService.listContents(authentication.getName(), request);
        ApiResponseFormat<CmsContentListResponseDto> response = new ApiResponseFormat<>(
                200,
                "CMS contents retrieved successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/contents/{contentId}")
    @Operation(summary = "Get CMS content by ID", description = "Get a single CMS content detail")
    public ResponseEntity<ApiResponseFormat<CmsContentDetailResponseDto>> getContentById(
            Authentication authentication,
            @PathVariable("contentId") String contentId) {
        CmsContentDetailResponseDto dto = cmsService.getContentById(authentication.getName(), contentId);
        ApiResponseFormat<CmsContentDetailResponseDto> response = new ApiResponseFormat<>(
                200,
                "CMS content retrieved successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

        @GetMapping(value = "/contents/{contentId}/html", produces = MediaType.TEXT_HTML_VALUE)
        @Operation(summary = "Get CMS content as HTML", description = "Render and return CMS content by ID in HTML format")
        public ResponseEntity<String> getContentHtmlById(
                        Authentication authentication,
                        @PathVariable("contentId") String contentId) {
                String html = cmsService.getContentHtmlById(authentication.getName(), contentId);
                return ResponseEntity.ok()
                                .contentType(MediaType.TEXT_HTML)
                                .body(html);
        }

        @GetMapping("/dashboard/recent-items")
        @Operation(summary = "Get dashboard recent items", description = "Get recent generations and CMS items")
        public ResponseEntity<ApiResponseFormat<CmsDashboardRecentItemsResponseDto>> getDashboardRecentItems(
                        Authentication authentication,
                        @RequestParam(name = "limit", required = false) Integer limit) {
                CmsDashboardRecentItemsResponseDto dto = cmsService.getDashboardRecentItems(authentication.getName(), limit);
                ApiResponseFormat<CmsDashboardRecentItemsResponseDto> response = new ApiResponseFormat<>(
                                200,
                                "Recent items retrieved successfully",
                                dto);
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @GetMapping("/dashboard/summary")
        @Operation(summary = "Get dashboard summary", description = "Get summary metrics for generations and CMS content")
        public ResponseEntity<ApiResponseFormat<CmsDashboardSummaryResponseDto>> getDashboardSummary(
                        Authentication authentication,
                        @RequestParam(name = "period", required = false) String period) {
                CmsDashboardSummaryResponseDto dto = cmsService.getDashboardSummary(authentication.getName(), period);
                ApiResponseFormat<CmsDashboardSummaryResponseDto> response = new ApiResponseFormat<>(
                                200,
                                "Dashboard summary retrieved successfully",
                                dto);
                return ResponseEntity.status(response.getStatus()).body(response);
        }

    @PatchMapping("/contents/{contentId}")
    @Operation(summary = "Update CMS content", description = "Update editable fields on a CMS content item")
    public ResponseEntity<ApiResponseFormat<CmsContentResponseDto>> updateContent(
            Authentication authentication,
            @PathVariable("contentId") String contentId,
            @RequestBody UpdateCmsContentRequestDto request) {
        CmsContentResponseDto dto = cmsService.updateContent(authentication.getName(), contentId, request);
        ApiResponseFormat<CmsContentResponseDto> response = new ApiResponseFormat<>(
                200,
                "CMS content updated successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/contents/{contentId}/publish")
    @Operation(summary = "Publish CMS content", description = "Change CMS content status to PUBLISHED")
    public ResponseEntity<ApiResponseFormat<CmsStatusChangeResponseDto>> publishContent(
            Authentication authentication,
            @PathVariable("contentId") String contentId) {
        CmsStatusChangeResponseDto dto = cmsService.publishContent(authentication.getName(), contentId);
        ApiResponseFormat<CmsStatusChangeResponseDto> response = new ApiResponseFormat<>(
                200,
                "CMS content published successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/contents/{contentId}/draft")
    @Operation(summary = "Move CMS content to draft", description = "Change CMS content status to DRAFT")
    public ResponseEntity<ApiResponseFormat<CmsStatusChangeResponseDto>> moveContentToDraft(
            Authentication authentication,
            @PathVariable("contentId") String contentId) {
        CmsStatusChangeResponseDto dto = cmsService.moveContentToDraft(authentication.getName(), contentId);
        ApiResponseFormat<CmsStatusChangeResponseDto> response = new ApiResponseFormat<>(
                200,
                "CMS content moved to draft successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/contents/{contentId}/archive")
    @Operation(summary = "Archive CMS content", description = "Change CMS content status to ARCHIVED")
    public ResponseEntity<ApiResponseFormat<CmsStatusChangeResponseDto>> archiveContent(
            Authentication authentication,
            @PathVariable("contentId") String contentId) {
        CmsStatusChangeResponseDto dto = cmsService.archiveContent(authentication.getName(), contentId);
        ApiResponseFormat<CmsStatusChangeResponseDto> response = new ApiResponseFormat<>(
                200,
                "CMS content archived successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/contents/{contentId}")
    @Operation(summary = "Delete CMS content", description = "Delete a CMS content owned by the current user")
    public ResponseEntity<ApiResponseFormat<Object>> deleteContent(
            Authentication authentication,
            @PathVariable("contentId") String contentId) {
        cmsService.deleteContent(authentication.getName(), contentId);
        ApiResponseFormat<Object> response = new ApiResponseFormat<>(
                200,
                "CMS content deleted successfully",
                null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
