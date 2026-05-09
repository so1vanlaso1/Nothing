package com.YeuTech.Infrastructure.Repository;

import com.YeuTech.Domain.Entities.PasswordResetToken;
import com.YeuTech.Domain.Repository.IPasswordResetTokenRepository;
import com.YeuTech.Infrastructure.Dao.PasswordResetTokenJpaDao;
import com.YeuTech.Infrastructure.Model.PasswordResetTokenJpaEntity;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepository implements IPasswordResetTokenRepository {
    private final PasswordResetTokenJpaDao dao;

    public PasswordResetTokenRepository(PasswordResetTokenJpaDao dao) {
        this.dao = dao;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenJpaEntity entity = toEntity(token);
        PasswordResetTokenJpaEntity saved = dao.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<PasswordResetToken> findById(String tokenId) {
        return dao.findById(tokenId).map(this::toDomain);
    }

    @Override
    public Optional<PasswordResetToken> findByUserIdAndTokenHash(String userId, String tokenHash) {
        return dao.findByUserIdAndTokenHash(userId, tokenHash).map(this::toDomain);
    }

    private PasswordResetTokenJpaEntity toEntity(PasswordResetToken d) {
        PasswordResetTokenJpaEntity e = new PasswordResetTokenJpaEntity();
        e.setTokenId(d.getTokenId());
        e.setUserId(d.getUserId());
        e.setTokenHash(d.getTokenHash());
        e.setExpiresAt(d.getExpiresAt());
        e.setUsed(d.isUsed());
        e.setUsedAt(d.getUsedAt());
        return e;
    }

    private PasswordResetToken toDomain(PasswordResetTokenJpaEntity e) {
        PasswordResetToken d = new PasswordResetToken();
        d.setTokenId(e.getTokenId());
        d.setUserId(e.getUserId());
        d.setTokenHash(e.getTokenHash());
        d.setExpiresAt(e.getExpiresAt());
        d.setUsed(e.isUsed());
        d.setUsedAt(e.getUsedAt());
        return d;
    }
}