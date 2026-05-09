package com.YeuTech.Domain.Repository;

import com.YeuTech.Domain.Entities.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository {
        void save(RefreshToken refreshToken);
        void deleteByUserId(String userId);
        Optional<RefreshToken> findByUserIdAndTokenHash(String userId, String tokenHash);
//        RefreshToken findByJti(String jti);
//        void revokeRefreshToken(String jti);
}
