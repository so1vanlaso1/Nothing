package com.YeuTech.Application.Services;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface IJwtService {
    String generateToken(String username);
    Boolean validateToken(String token, UserDetails userDetails);
    String generateRefreshToken(String username);
    String extractJti(String token);
    Date extractIssuedAt(String token);
    String extractUsername(String token);
    Date extractExpiration(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String hashToken(String rawToken);
    String generatePasswordResetToken(String userId, String tokenId);
}