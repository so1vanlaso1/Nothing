package com.YeuTech.Application.Services;

import java.time.ZoneId;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YeuTech.Application.Mappers.UserDtoMapper;
import com.YeuTech.Application.Utils.OtpGenerator;
import com.YeuTech.Application.Utils.PasswordHelper;
import com.YeuTech.Application.Utils.UserValidator;
import com.YeuTech.Domain.Entities.EmailOtp;
import com.YeuTech.Domain.Entities.RefreshToken;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Entities.UserProfile;
import com.YeuTech.Domain.Repository.IEmailOtpRepository;
import com.YeuTech.Domain.Repository.IRefreshTokenRepository;
import com.YeuTech.Domain.Repository.IUserRepository;
import com.YeuTech.Domain.Repository.IUserProfileRepository;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.ActiveRequestDto;
import com.YeuTech.Dtos.Request.ReactiveRequestDto;
import com.YeuTech.Dtos.Request.RefreshTokenRequestDto;
import com.YeuTech.Dtos.Request.RegisterRequestDto;
import com.YeuTech.Dtos.Response.AuthResponseDto;

@Service
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final IUserProfileRepository userProfileRepository;
    private final IEmailOtpRepository emailOtpRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    private final PasswordHelper passwordHelper;
    private final UserValidator userValidator;
    private final IMailService mailService;

    public AuthService(IUserRepository userRepository, IUserProfileRepository userProfileRepository,
            IEmailOtpRepository emailOtpRepository,
            IRefreshTokenRepository refreshTokenRepository, AuthenticationManager authenticationManager,
            IJwtService jwtService, PasswordHelper passwordHelper,
            UserValidator userValidator, IMailService mailService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.emailOtpRepository = emailOtpRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordHelper = passwordHelper;
        this.userValidator = userValidator;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public ApiResponseFormat<AuthResponseDto> register(RegisterRequestDto dto) {
        // check email format
        String result = User.validateEmail(dto.email());
        if (result != null) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), result, null);
        }

        // check email exists
        result = userValidator.checkEmailExists(dto.email());
        if (result != null) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), result, null);
        }

        // check password length
        result = User.validatePassword(dto.password());
        if (result != null) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), result, null);
        }

        // Save record auth_user
        String encodedPassword = passwordHelper.encode(dto.password());
        User user = UserDtoMapper.toDomain(dto, encodedPassword);
        userRepository.save(user);

        // save record user_profiles
        String nameFromEmail = dto.email().split("@")[0];

        UserProfile userProfile = new UserProfile(user.getId(), nameFromEmail, nameFromEmail, null);
        userProfileRepository.save(userProfile);

        // create opt code and save record email_otp
        String otpCode = OtpGenerator.generateOtp(6);
        EmailOtp emailOtp = new EmailOtp(user.getId(), otpCode, 5);
        emailOtpRepository.save(emailOtp);

        // send email
        try {
            mailService.sendRegisterOtpEmail(dto.email(), otpCode, dto.language());
        } catch (Exception e) {
            return new ApiResponseFormat<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to send verification email",
                    null);
        }

        return new ApiResponseFormat<>(HttpStatus.CREATED.value(), "Register successfully!", null);
    }

    @Override
    @Transactional
    public ApiResponseFormat<AuthResponseDto> login(RegisterRequestDto dto) {
        String result = verify(dto);

        if (result == null || result.equals("Authentication failed")) {
            return new ApiResponseFormat<>(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", null);
        }

        User user = userValidator.findByEmailNormalized(dto.email()).orElse(null);
        if (user == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "User not exists", null);
        }

        if (!user.isEmailVerified()) {
            return new ApiResponseFormat<>(HttpStatus.UNAUTHORIZED.value(), "Account not activated yet", null);
        }

        if (!user.isActive()) {
            return new ApiResponseFormat<>(HttpStatus.UNAUTHORIZED.value(), "Account is disabled", null);
        }

        // delete old refresh token
        refreshTokenRepository.deleteByUserId(user.getId());

        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        String tokenHash = jwtService.hashToken(refreshToken);
        String jti = jwtService.extractJti(refreshToken);
        Date issuedAt = jwtService.extractIssuedAt(refreshToken);
        Date expiresAt = jwtService.extractExpiration(refreshToken);

        RefreshToken rt = new RefreshToken(
                user.getId(),
                tokenHash,
                jti,
                issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        refreshTokenRepository.save(rt);

        AuthResponseDto authResponse = new AuthResponseDto(user.getEmail(), accessToken, refreshToken, expiresAt);
        return new ApiResponseFormat<>(HttpStatus.OK.value(), "Login successfully!", authResponse);
    }

    private String verify(RegisterRequestDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(dto.email());
            }
        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        }
        return "Authentication failed";
    }

    @Override
    @Transactional
    public ApiResponseFormat<AuthResponseDto> refreshToken(RefreshTokenRequestDto dto) {
        // validate refresh token
        String email;
        try {
            email = jwtService.extractUsername(dto.refreshToken());
        } catch (Exception e) {
            return new ApiResponseFormat<>(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token", null);
        }

        // find user
        User user = userValidator.findByEmailNormalized(email).orElse(null);
        if (user == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "User not exists", null);
        }

        // hask token + find to db
        String tokenHash = jwtService.hashToken(dto.refreshToken());
        RefreshToken storedToken = refreshTokenRepository.findByUserIdAndTokenHash(user.getId(), tokenHash)
                .orElse(null);

        if (storedToken == null) {
            return new ApiResponseFormat<>(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token", null);
        }

        if (storedToken.isRevoked()) {
            return new ApiResponseFormat<>(HttpStatus.UNAUTHORIZED.value(), "Refresh token has been revoked", null);
        }

        if (storedToken.isExpired()) {
            return new ApiResponseFormat<>(HttpStatus.UNAUTHORIZED.value(), "Refresh token has expired", null);
        }

        // delete old refresh token + create new access and refresh token
        refreshTokenRepository.deleteByUserId(user.getId());

        String newAccessToken = jwtService.generateToken(email);
        String newRefreshToken = jwtService.generateRefreshToken(email);

        String newTokenHash = jwtService.hashToken(newRefreshToken);
        String jti = jwtService.extractJti(newRefreshToken);
        Date issuedAt = jwtService.extractIssuedAt(newRefreshToken);
        Date expiresAt = jwtService.extractExpiration(newRefreshToken);

        RefreshToken newRt = new RefreshToken(
                user.getId(),
                newTokenHash,
                jti,
                issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        refreshTokenRepository.save(newRt);

        AuthResponseDto authResponse = new AuthResponseDto(email, newAccessToken, newRefreshToken, expiresAt);
        return new ApiResponseFormat<>(HttpStatus.OK.value(), "Tokens refreshed successfully!", authResponse);
    }

    @Override
    @Transactional
    public ApiResponseFormat<AuthResponseDto> activeAccount(ActiveRequestDto dto) {
        // find user by email
        User user = userValidator.findByEmailNormalized(dto.email()).orElse(null);
        if (user == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "User not exists", null);
        }

        // check verified email
        if (user.isEmailVerified()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Account already active", null);
        }

        // find opt active
        EmailOtp otp = emailOtpRepository.findActiveByUserId(user.getId()).orElse(null);
        if (otp == null) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "No active OTP found", null);
        }

        if (otp.isExpired()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "OTP has expired", null);
        }

        // Verify OTP
        if (!otp.verify(dto.code())) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Invalid OTP", null);
        }

        // mark otp
        otp.markAsUsed();
        emailOtpRepository.save(otp);

        // activate account
        user.setEmailVerified(true);
        user.setActive(true);
        userRepository.save(user);

        // Create tokens for new user
        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        String tokenHash = jwtService.hashToken(refreshToken);
        String jti = jwtService.extractJti(refreshToken);
        Date issuedAt = jwtService.extractIssuedAt(refreshToken);
        Date expiresAt = jwtService.extractExpiration(refreshToken);

        RefreshToken rt = new RefreshToken(
                user.getId(),
                tokenHash,
                jti,
                issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        refreshTokenRepository.save(rt);

        AuthResponseDto authResponse = new AuthResponseDto(user.getEmail(), accessToken, refreshToken, expiresAt);
        return new ApiResponseFormat<>(HttpStatus.CREATED.value(), "Active successfully!", authResponse);
    }

    @Override
    @Transactional
    public ApiResponseFormat<AuthResponseDto> reactiveAccount(ReactiveRequestDto dto) {
        // find user by email
        User user = userValidator.findByEmailNormalized(dto.email()).orElse(null);
        if (user == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "User not exists", null);
        }

        // check verified email
        if (user.isEmailVerified()) {
            return new ApiResponseFormat<>(HttpStatus.BAD_REQUEST.value(), "Account already active", null);
        }

        emailOtpRepository.deleteByUserId(user.getId());

        // create opt code and save record email_otp
        String otpCode = OtpGenerator.generateOtp(6);
        EmailOtp emailOtp = new EmailOtp(user.getId(), otpCode, 5);
        emailOtpRepository.save(emailOtp);

        // send email
        try {
            mailService.sendRegisterOtpEmail(dto.email(), otpCode, dto.language());
        } catch (Exception e) {
            return new ApiResponseFormat<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to send verification email",
                    null);
        }

        return new ApiResponseFormat<>(HttpStatus.OK.value(), "Re-active successfully!", null);
    }
}
