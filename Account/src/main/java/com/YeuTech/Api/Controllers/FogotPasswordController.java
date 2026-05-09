package com.YeuTech.Api.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.IPasswordResetService;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.ForgotPasswordRequestDto;
import com.YeuTech.Dtos.Request.ResetPasswordRequestDto;
import com.YeuTech.Dtos.Request.VerifyOtpRequestDto;
import com.YeuTech.Dtos.Response.ForgotPasswordResponseDto;

@RestController
@RequestMapping("/v1/api/forgot")
@Tag(name = "Password Reset", description = "Password reset and OTP verification APIs")
public class FogotPasswordController {
    private final IPasswordResetService _passwordResetService;

    public FogotPasswordController(IPasswordResetService passwordResetService) {
        this._passwordResetService = passwordResetService;
    }

    @PostMapping("/request")
    @Operation(summary = "Request password reset", description = "Send OTP and initialize password reset flow")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reset request accepted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Email not found")
    })
    public ResponseEntity<ApiResponseFormat<Object>> requestPasswordReset(@RequestBody ForgotPasswordRequestDto dto) {
        ApiResponseFormat<Object> response = _passwordResetService.requestPasswordReset(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify OTP", description = "Verify OTP and return verification token for password reset")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP verified successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    public ResponseEntity<ApiResponseFormat<ForgotPasswordResponseDto>> verifyOtp(@RequestBody VerifyOtpRequestDto dto) {
        ApiResponseFormat<ForgotPasswordResponseDto> response = _passwordResetService.verifyOtp(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset password", description = "Reset password using verification token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid verification token")
    })
    public ResponseEntity<ApiResponseFormat<Object>> resetPassword(@RequestBody ResetPasswordRequestDto dto) {
        ApiResponseFormat<Object> response = _passwordResetService.resetPassword(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

