package com.YeuTech.Infrastructure.Repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.YeuTech.Domain.Entities.SocialMediaConnection;
import com.YeuTech.Domain.Enums.SocialPlatform;
import com.YeuTech.Domain.Repository.ISocialMediaConnectionRepository;
import com.YeuTech.Infrastructure.Dao.SocialMediaConnectionJpaDao;
import com.YeuTech.Infrastructure.Mappers.SocialMediaConnectionMapper;
import com.YeuTech.Infrastructure.Model.SocialMediaConnectionJpaEntity;

@Repository
public class SocialMediaConnectionRepository implements ISocialMediaConnectionRepository {

    private final SocialMediaConnectionJpaDao socialMediaConnectionJpaDao;

    public SocialMediaConnectionRepository(SocialMediaConnectionJpaDao socialMediaConnectionJpaDao) {
        this.socialMediaConnectionJpaDao = socialMediaConnectionJpaDao;
    }

    @Override
    public Optional<SocialMediaConnection> findByUserIdAndPlatform(String userId, SocialPlatform platform) {
        return socialMediaConnectionJpaDao.findByUserIdAndPlatform(userId, platform.name())
                .map(SocialMediaConnectionMapper::toDomain);
    }

    @Override
    public void save(SocialMediaConnection connection) {
        SocialMediaConnectionJpaEntity entity = SocialMediaConnectionMapper.toEntity(connection);
        if (entity == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        socialMediaConnectionJpaDao.save(entity);
    }
}
