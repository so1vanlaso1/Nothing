package com.YeuTech.Infrastructure.Repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.YeuTech.Domain.Entities.UserProfile;
import com.YeuTech.Domain.Repository.IUserProfileRepository;
import com.YeuTech.Infrastructure.Dao.UserProfileJpaDao;
import com.YeuTech.Infrastructure.Mappers.UserProfileMapper;
import com.YeuTech.Infrastructure.Model.UserProfileJpaEntity;

@Repository
public class UserProfileRepository implements IUserProfileRepository {
    private final UserProfileJpaDao userProfileJpaDao;

    public UserProfileRepository(UserProfileJpaDao userProfileJpaDao) {
        this.userProfileJpaDao = userProfileJpaDao;
    }

    @Override
    public void save(UserProfile userProfile) {
        UserProfileJpaEntity entity = UserProfileMapper.toEntity(userProfile);
        userProfileJpaDao.save(entity);
    }

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        return userProfileJpaDao.findById(userId).map(UserProfileMapper::toDomain);
    }
}
