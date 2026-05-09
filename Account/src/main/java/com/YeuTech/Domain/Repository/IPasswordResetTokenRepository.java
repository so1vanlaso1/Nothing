package com.YeuTech.Domain.Repository;

import java.util.Optional;

import com.YeuTech.Domain.Entities.PasswordResetToken;

public interface IPasswordResetTokenRepository {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findById(String tokenId);
    Optional<PasswordResetToken> findByUserIdAndTokenHash(String userId, String tokenHash);
}