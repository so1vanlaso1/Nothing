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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.IFacebookService;
import com.YeuTech.Application.Services.IMarketingService;
import com.YeuTech.Application.Services.IUserService;
import com.YeuTech.Application.Utils.SecurityUtils;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.FacebookPostRequest;
import com.YeuTech.Dtos.Request.UpdateMarketingGenerationRequestDto;
import com.YeuTech.Dtos.Response.CmsDashboardRecentItemsResponseDto;
import com.YeuTech.Dtos.Response.MarketingGenerationResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/marketing")
@Tag(name = "Marketing", description = "Marketing APIs")
@SecurityRequirement(name = "bearerAuth")
public class MarketingController {

    private final IMarketingService _marketingService;
    private final IFacebookService _facebookService;
    private final IUserService _userService;

    public MarketingController(IMarketingService marketingService,
            IFacebookService facebookPublishingService,
            IUserService userService) {
        this._marketingService = marketingService;
        this._facebookService = facebookPublishingService;
        this._userService = userService;
    }

    @GetMapping("/status")
    @Operation(summary = "Marketing status", description = "Simple endpoint to verify marketing API availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marketing API is available")
    })
    public String status() {
        return "OK";
    }

    @GetMapping("/alll")
    @Operation(summary = "Get all marketing", description = "Demo endpoint that retrieves all marketing generations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marketing data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<MarketingGenerationResponseDto> getAllMarketing() {
        return _marketingService.getAllMarketing();
    }

    @GetMapping("/all")
    @Operation(summary = "Get marketing by user ID", description = "Retrieves marketing generations for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marketing data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<MarketingGenerationResponseDto> getMarketingByUserId() {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = _userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return _marketingService.getMarketingByUserId(user.getId());
    }

    @GetMapping("/generation/{generationId}")
    @Operation(summary = "Get marketing by generation ID", description = "Retrieves a single marketing generation owned by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marketing data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Marketing generation not found")
    })
    public ResponseEntity<ApiResponseFormat<MarketingGenerationResponseDto>> getMarketingByGenerationId(
            Authentication authentication,
            @PathVariable("generationId") String generationId) {
        User user = _userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + authentication.getName()));

        MarketingGenerationResponseDto dto = _marketingService
                .getMarketingByGenerationIdAndUserId(generationId, user.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Marketing generation not found for user: " + user.getId() + ", generationId: " + generationId));

        ApiResponseFormat<MarketingGenerationResponseDto> response = new ApiResponseFormat<>(
                200,
                "Marketing generation retrieved successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/generation/{generationId}")
    @Operation(summary = "Patch marketing generation", description = "Partially updates a marketing generation owned by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marketing generation updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Marketing generation not found")
    })
    public ResponseEntity<ApiResponseFormat<MarketingGenerationResponseDto>> patchMarketingByGenerationId(
            Authentication authentication,
            @PathVariable("generationId") String generationId,
            @RequestBody UpdateMarketingGenerationRequestDto request) {
        User user = _userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + authentication.getName()));

        MarketingGenerationResponseDto dto = _marketingService
                .patchMarketingByGenerationIdAndUserId(generationId, user.getId(), request);

        ApiResponseFormat<MarketingGenerationResponseDto> response = new ApiResponseFormat<>(
                200,
                "Marketing generation updated successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/dashboard/recent-items")
    @Operation(summary = "Get recent marketing generations", description = "Retrieves recent marketing generations for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recent items retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponseFormat<CmsDashboardRecentItemsResponseDto>> getRecentItems(
            Authentication authentication,
            @RequestParam(name = "limit", required = false) Integer limit) {
        User user = _userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + authentication.getName()));

        CmsDashboardRecentItemsResponseDto dto = _marketingService.getRecentItems(user.getId(), limit);
        ApiResponseFormat<CmsDashboardRecentItemsResponseDto> response = new ApiResponseFormat<>(
                200,
                "Recent items retrieved successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping("/generate")
    @Operation(summary = "Save marketing content", description = "Saves new marketing content for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marketing content saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponseFormat<MarketingGenerationResponseDto>> generateMarketing(
            Authentication authentication,
            @RequestBody @Valid UpdateMarketingGenerationRequestDto request) {
        User user = _userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + authentication.getName()));

        MarketingGenerationResponseDto dto = _marketingService.createMarketing(user.getId(), request);
        ApiResponseFormat<MarketingGenerationResponseDto> response = new ApiResponseFormat<>(
                200,
                "Marketing content saved successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    


    @PostMapping("/facebook/publish")
    @Operation(summary = "Publish post to Facebook", description = "Publishes a message to the connected Facebook page for the current user using stored credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Publishing failed")
    })
    public ResponseEntity<ApiResponseFormat<Void>> publishToFacebook(
            @RequestBody @Valid FacebookPostRequest request) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = _userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        _facebookService.publishToFacebookByUserId(user.getId(), request.message());

        return ResponseEntity.ok(new ApiResponseFormat<>(200, "Posted successfully to Facebook Page", null));
    }

    @GetMapping("/facebook/pages")
    @Operation(summary = "Get Facebook page info", description = "Retrieve information about the connected Facebook page for the current user")
    public ResponseEntity<ApiResponseFormat<Object>> getFacebookPageInfo() {
        String email = SecurityUtils.getCurrentUserEmail();
        ApiResponseFormat<Object> response = _facebookService.getPageInfo(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
