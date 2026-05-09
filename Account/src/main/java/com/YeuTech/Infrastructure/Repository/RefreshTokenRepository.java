package com.YeuTech.Infrastructure.Repository;

import com.YeuTech.Domain.Entities.RefreshToken;
import com.YeuTech.Domain.Repository.IRefreshTokenRepository;
import com.YeuTech.Infrastructure.Dao.RefreshTokenJpaDao;
import com.YeuTech.Infrastructure.Mappers.RefreshTokenMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepository implements IRefreshTokenRepository {
    private final RefreshTokenJpaDao refreshTokenJpaDao;

    public RefreshTokenRepository(RefreshTokenJpaDao refreshTokenJpaDao) {
        this.refreshTokenJpaDao = refreshTokenJpaDao;
    }

    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenJpaDao.save(RefreshTokenMapper.toEntity(refreshToken));
    }

    @Override
    public void deleteByUserId(String userId) {
        refreshTokenJpaDao.deleteByUserId(userId);
    }

    @Override
    public Optional<RefreshToken> findByUserIdAndTokenHash(String userId, String tokenHash) {
        return refreshTokenJpaDao.findByUserIdAndTokenHash(userId, tokenHash)
                .map(RefreshTokenMapper::toDomain);
    }
}
