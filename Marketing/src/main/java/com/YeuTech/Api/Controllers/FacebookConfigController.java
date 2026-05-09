package com.YeuTech.Api.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.IFacebookConfigService;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.TokenStatusDto;
import com.YeuTech.Dtos.Request.FacebookTokenUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/marketing/config/facebook")
@Tag(name = "Marketing Config", description = "Endpoints for configuring social media connections")
public class FacebookConfigController {

        private final IFacebookConfigService facebookConfigService;

        public FacebookConfigController(IFacebookConfigService facebookService) {
                this.facebookConfigService = facebookService;
        }

        @GetMapping("/token/status")
        @Operation(summary = "Get Facebook token status", description = "Checks whether a Facebook connection exists for the current user and if the stored token is valid or expired.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved token status", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.YeuTech.Dtos.ApiResponseFormat.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized - User must be logged in", content = @Content)
        })
        public ResponseEntity<ApiResponseFormat<TokenStatusDto>> getTokenStatus(
                        @AuthenticationPrincipal UserDetails userDetails) {

                ApiResponseFormat<TokenStatusDto> response = facebookConfigService
                                .getTokenStatus(userDetails.getUsername());
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @PutMapping("/token")
        @Operation(summary = "Update Facebook access token", description = "Updates or creates the Facebook connection for the current user. "
                        + "Can handle short-lived token exchange for long-lived ones if requested.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Facebook token updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid token or request data"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
        public ResponseEntity<ApiResponseFormat<Void>> updateAccessToken(
                        @AuthenticationPrincipal UserDetails userDetails,
                        @RequestBody @Valid FacebookTokenUpdateRequest dto) {

                ApiResponseFormat<Void> response = facebookConfigService.updateAccessToken(
                                userDetails.getUsername(), dto);
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @PostMapping("/token/refresh")
        @Operation(summary = "Manually refresh Facebook token", description = "Attempts to refresh the Page Access Token using the stored long-lived user token. "
                        + "Only works if a long-lived user token was previously stored.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
                        @ApiResponse(responseCode = "400", description = "Manual refresh not possible (e.g., user token expired)"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
        public ResponseEntity<ApiResponseFormat<Void>> refreshToken(
                        @AuthenticationPrincipal UserDetails userDetails) {
                ApiResponseFormat<Void> response = facebookConfigService.refreshToken(userDetails.getUsername());
                return ResponseEntity.status(response.getStatus()).body(response);
        }
}
