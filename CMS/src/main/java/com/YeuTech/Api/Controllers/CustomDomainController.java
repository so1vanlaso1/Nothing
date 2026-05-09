package com.YeuTech.Api.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.ICustomDomainService;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.PublishContentToDomainRequestDto;
import com.YeuTech.Dtos.Request.RegisterDomainRequestDto;
import com.YeuTech.Dtos.Response.ContentPublicationResponseDto;
import com.YeuTech.Dtos.Response.CustomDomainResponseDto;
import com.YeuTech.Dtos.Response.DomainDnsCheckResponseDto;
import com.YeuTech.Dtos.Response.DomainVerificationResponseDto;
import com.YeuTech.Dtos.Response.PublicUrlResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/cms")
@Tag(name = "Custom Domains", description = "Custom domain registration and content publishing APIs")
@SecurityRequirement(name = "bearerAuth")
public class CustomDomainController {

    private final ICustomDomainService domainService;

    public CustomDomainController(ICustomDomainService domainService) {
        this.domainService = domainService;
    }

    @PostMapping("/domains")
    @Operation(summary = "Register custom domain",
            description = "Register a new custom domain for publishing. Returns a verification token for DNS TXT record setup.")
    public ResponseEntity<ApiResponseFormat<CustomDomainResponseDto>> registerDomain(
            Authentication authentication,
            @RequestBody RegisterDomainRequestDto request) {
        CustomDomainResponseDto dto = domainService.registerDomain(authentication.getName(), request);
        ApiResponseFormat<CustomDomainResponseDto> response = new ApiResponseFormat<>(
                201,
                "Domain registered successfully. Add TXT record to verify ownership.",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/domains")
    @Operation(summary = "List custom domains",
            description = "List all custom domains registered by the current user")
    public ResponseEntity<ApiResponseFormat<List<CustomDomainResponseDto>>> listDomains(
            Authentication authentication) {
        List<CustomDomainResponseDto> list = domainService.listDomains(authentication.getName());
        ApiResponseFormat<List<CustomDomainResponseDto>> response = new ApiResponseFormat<>(
                200,
                "Domains retrieved successfully",
                list);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/domains/{domainId}/verify")
    @Operation(summary = "Verify domain ownership",
            description = "Trigger DNS TXT record verification for domain ownership")
    public ResponseEntity<ApiResponseFormat<DomainVerificationResponseDto>> verifyDomain(
            Authentication authentication,
            @PathVariable("domainId") String domainId) {
        DomainVerificationResponseDto dto = domainService.verifyDomain(authentication.getName(), domainId);
        ApiResponseFormat<DomainVerificationResponseDto> response = new ApiResponseFormat<>(
                200,
                "Domain verification completed",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/domains/{domainId}/dns-check")
    @Operation(summary = "Check DNS pointing",
            description = "Check if domain DNS records are pointing to YeuTech infrastructure")
    public ResponseEntity<ApiResponseFormat<DomainDnsCheckResponseDto>> dnsCheck(
            Authentication authentication,
            @PathVariable("domainId") String domainId) {
        DomainDnsCheckResponseDto dto = domainService.dnsCheck(authentication.getName(), domainId);
        ApiResponseFormat<DomainDnsCheckResponseDto> response = new ApiResponseFormat<>(
                200,
                "DNS check completed",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/domains/{domainId}/activate")
    @Operation(summary = "Activate domain",
            description = "Activate a verified and DNS-pointed domain for content publishing")
    public ResponseEntity<ApiResponseFormat<CustomDomainResponseDto>> activateDomain(
            Authentication authentication,
            @PathVariable("domainId") String domainId) {
        CustomDomainResponseDto dto = domainService.activateDomain(authentication.getName(), domainId);
        ApiResponseFormat<CustomDomainResponseDto> response = new ApiResponseFormat<>(
                200,
                "Domain activated successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/contents/{contentId}/publish-domain")
    @Operation(summary = "Publish content to domain",
            description = "Publish a CMS content item to a custom domain. Content must be in PUBLISHED status.")
    public ResponseEntity<ApiResponseFormat<ContentPublicationResponseDto>> publishContentToDomain(
            Authentication authentication,
            @PathVariable("contentId") String contentId,
            @RequestBody PublishContentToDomainRequestDto request) {
        ContentPublicationResponseDto dto = domainService.publishContentToDomain(
                authentication.getName(), contentId, request);
        ApiResponseFormat<ContentPublicationResponseDto> response = new ApiResponseFormat<>(
                201,
                "Content published to domain successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/contents/{contentId}/public-url")
    @Operation(summary = "Get public URL",
            description = "Get the public URL for a content item published to a custom domain")
    public ResponseEntity<ApiResponseFormat<PublicUrlResponseDto>> getPublicUrl(
            Authentication authentication,
            @PathVariable("contentId") String contentId) {
        PublicUrlResponseDto dto = domainService.getPublicUrl(authentication.getName(), contentId);
        ApiResponseFormat<PublicUrlResponseDto> response = new ApiResponseFormat<>(
                200,
                "Public URL retrieved successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
