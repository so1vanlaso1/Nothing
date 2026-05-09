package com.YeuTech.Application.Services;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.ForgotPasswordRequestDto;
import com.YeuTech.Dtos.Request.ResetPasswordRequestDto;
import com.YeuTech.Dtos.Request.VerifyOtpRequestDto;
import com.YeuTech.Dtos.Response.ForgotPasswordResponseDto;
    
/**
 * Interface for password reset operations
 */
public interface IPasswordResetService {

    ApiResponseFormat<Object> requestPasswordReset(ForgotPasswordRequestDto dto);

    ApiResponseFormat<ForgotPasswordResponseDto> verifyOtp(VerifyOtpRequestDto dto);

    ApiResponseFormat<Object> resetPassword(ResetPasswordRequestDto dto);
}
