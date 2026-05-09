package com.YeuTech.Api.Controllers;

import com.YeuTech.Dtos.Request.ActiveRequestDto;
import com.YeuTech.Dtos.Request.ReactiveRequestDto;
import com.YeuTech.Dtos.Request.RefreshTokenRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.IAuthService;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.RegisterRequestDto;
import com.YeuTech.Dtos.Response.AuthResponseDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/api/auth")
@Tag(name = "Authentication", description = "Authentication and account lifecycle APIs")
public class AuthController {

    private final IAuthService _authService;

    public AuthController(IAuthService authService) {
        this._authService = authService;
    }

    @GetMapping("/status")
    @Operation(summary = "Health status", description = "Simple endpoint to verify authentication API availability")
    public String getStatus(HttpServletRequest request) {
        return "OK";
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register a new user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"status\": 201,\n  \"message\": \"Register successfully\",\n  \"data\": {\n    \"email\": \"user@example.com\",\n    \"accessToken\": \"eyJhbGciOi...\",\n    \"refreshToken\": \"eyJhbGciOi...\",\n    \"expiryTime\": \"2026-04-02T12:00:00.000+00:00\"\n  },\n  \"timestamp\": \"2026-04-02T12:00:00\"\n}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    public ResponseEntity<ApiResponseFormat<AuthResponseDto>> register(@RequestBody RegisterRequestDto dto) {
        ApiResponseFormat<AuthResponseDto> response = _authService.register(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with email and password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponseFormat<AuthResponseDto>> login(@RequestBody RegisterRequestDto dto) {
        ApiResponseFormat<AuthResponseDto> response = _authService.login(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh token", description = "Issue a new access token using a valid refresh token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<ApiResponseFormat<AuthResponseDto>> refreshToken(@RequestBody RefreshTokenRequestDto dto) {
        ApiResponseFormat<AuthResponseDto> response = _authService.refreshToken(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/active")
    @Operation(summary = "Activate account", description = "Activate account using verification code")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Account activated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid activation code")
    })
    public ResponseEntity<ApiResponseFormat<AuthResponseDto>> activeAccount(@RequestBody ActiveRequestDto dto) {
        ApiResponseFormat<AuthResponseDto> response = _authService.activeAccount(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/reactive")
    @Operation(summary = "Resend activation", description = "Regenerate and resend account activation code")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Activation code resent"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<ApiResponseFormat<AuthResponseDto>> reactiveAccount(@RequestBody ReactiveRequestDto dto) {
        ApiResponseFormat<AuthResponseDto> response = _authService.reactiveAccount(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

