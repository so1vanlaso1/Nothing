package com.YeuTech.Application.Services;

import java.util.List;

import com.YeuTech.Dtos.Request.PublishContentToDomainRequestDto;
import com.YeuTech.Dtos.Request.RegisterDomainRequestDto;
import com.YeuTech.Dtos.Response.ContentPublicationResponseDto;
import com.YeuTech.Dtos.Response.CustomDomainResponseDto;
import com.YeuTech.Dtos.Response.DomainDnsCheckResponseDto;
import com.YeuTech.Dtos.Response.DomainVerificationResponseDto;
import com.YeuTech.Dtos.Response.PublicUrlResponseDto;

public interface ICustomDomainService {

    CustomDomainResponseDto registerDomain(String currentUserEmail, RegisterDomainRequestDto request);

    List<CustomDomainResponseDto> listDomains(String currentUserEmail);

    DomainVerificationResponseDto verifyDomain(String currentUserEmail, String domainId);

    DomainDnsCheckResponseDto dnsCheck(String currentUserEmail, String domainId);

    CustomDomainResponseDto activateDomain(String currentUserEmail, String domainId);

    ContentPublicationResponseDto publishContentToDomain(
            String currentUserEmail, String contentId, PublishContentToDomainRequestDto request);

    PublicUrlResponseDto getPublicUrl(String currentUserEmail, String contentId);
}
