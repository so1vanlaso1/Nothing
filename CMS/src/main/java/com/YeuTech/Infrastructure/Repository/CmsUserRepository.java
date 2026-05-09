package com.YeuTech.Infrastructure.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Repository.ICmsUserRepository;
import com.YeuTech.Infrastructure.Dao.CmsUserJpaDao;

@Repository
public class CmsUserRepository implements ICmsUserRepository {
    private final CmsUserJpaDao cmsUserJpaDao;

    public CmsUserRepository(CmsUserJpaDao cmsUserJpaDao) {
        this.cmsUserJpaDao = cmsUserJpaDao;
    }

    @Override
    public Optional<String> findUserIdByEmailNormalized(String emailNormalized) {
        return cmsUserJpaDao.findByEmailNormalized(emailNormalized)
                .map(user -> user.getUserId());
    }
}
