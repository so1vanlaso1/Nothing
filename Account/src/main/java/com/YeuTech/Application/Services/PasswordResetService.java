package com.YeuTech.Application.Services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YeuTech.Application.Utils.OtpGenerator;
import com.YeuTech.Application.Utils.PasswordHelper;
import com.YeuTech.Application.Utils.UserValidator;
import com.YeuTech.Domain.Entities.PasswordResetToken;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Repository.IPasswordResetTokenRepository;
import com.YeuTech.Domain.Repository.IUserRepository;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.ForgotPasswordRequestDto;
import com.YeuTech.Dtos.Request.ResetPasswordRequestDto;
import com.YeuTech.Dtos.Request.VerifyOtpRequestDto;
import com.YeuTech.Dtos.Response.ForgotPasswordResponseDto;

@Service
public class PasswordResetService implements IPasswordResetService {

    private final IPasswordResetTokenRepository passwordResetRepository;
    private final IUserRepository userRepository;
    private final IMailService mailService;
    private final UserValidator userValidator;
    private final PasswordHelper passwordHelper;
    private final IJwtService jwtService;

    public PasswordResetService(IPasswordResetTokenRepository passwordResetRepository, IUserRepository userRepository,
            IMailService mailService, UserValidator userValidator, PasswordHelper passwordHelper,
            IJwtService jwtService) {
        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userValidator = userValidator;
        this.passwordHelper = passwordHelper;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public ApiResponseFormat<Object> requestPasswordReset(ForgotPasswordRequestDto dto) {
        Optional<User> userOpt = userValidator.findByEmailNormalized(dto.email());
        if (userOpt.isEmpty()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Email not found", null);
        }

        User user = userOpt.get();

        PasswordResetToken token = new PasswordResetToken();
        token.setTokenId(UUID.randomUUID().toString());
        token.setUserId(user.getId());
        String rawOtp = OtpGenerator.generateOtp(6);
        token.setTokenHash(rawOtp);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15)); // expires in 15 minutes
        passwordResetRepository.save(token);
        try {
            mailService.sendPasswordResetOtpEmail(user.getEmail(), rawOtp, dto.language());
        } catch (Exception e) {
            // Handle email sending failure (e.g., log the error)
            System.err.println("Failed to send email: " + e.getMessage());
            return new ApiResponseFormat<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to send email", null);
        }

        return new ApiResponseFormat<>(HttpStatus.OK.value(), "Password reset token sent to email", null);

    }

    @Override
    public ApiResponseFormat<ForgotPasswordResponseDto> verifyOtp(VerifyOtpRequestDto dto) {
        Optional<User> userOpt = userValidator.findByEmailNormalized(dto.email());
        if (userOpt.isEmpty()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Email not found", null);
        }
        User user = userOpt.get();
        Optional<PasswordResetToken> tokenOpt = passwordResetRepository.findByUserIdAndTokenHash(user.getId(),
                dto.otp());
        if (tokenOpt.isEmpty()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Invalid OTP", null);
        }
        PasswordResetToken token = tokenOpt.get();

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "OTP has expired", null);
        }

        String jwtToken = jwtService.generatePasswordResetToken(user.getEmail(), token.getTokenId());

        return new ApiResponseFormat<>(HttpStatus.OK.value(), "OTP verified", new ForgotPasswordResponseDto(jwtToken));

    }

    @Override
    @Transactional
    public ApiResponseFormat<Object> resetPassword(ResetPasswordRequestDto dto) {
        String email = jwtService.extractUsername(dto.verificationToken());
        Optional<User> userOpt = userValidator.findByEmailNormalized(email);
        if (userOpt.isEmpty()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Invalid token", null);
        }
        User user = userOpt.get();

        String tokenId = jwtService.extractClaim(dto.verificationToken(), claims -> (String) claims.get("tokenId"));

        Optional<PasswordResetToken> tokenOpt = passwordResetRepository.findById(tokenId);
        if (tokenOpt.isEmpty()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Invalid token", null);
        }

        PasswordResetToken token = tokenOpt.get();

        String result = User.validatePassword(dto.newPassword());
        if (result != null) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), result, null);
        }

        user.setPassword(passwordHelper.encode(dto.newPassword()));
        userRepository.save(user);

        token.setUsed(true);
        token.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(token);

        return new ApiResponseFormat<>(HttpStatus.OK.value(), "Password reset successfully", null);
    }
}

