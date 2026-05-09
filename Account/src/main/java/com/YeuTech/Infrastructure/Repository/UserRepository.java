package com.YeuTech.Infrastructure.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Repository.IUserRepository;
import com.YeuTech.Infrastructure.Dao.UserJpaDao;
import com.YeuTech.Infrastructure.Mappers.UserMapper;
import com.YeuTech.Infrastructure.Model.UserJpaEntity;

@Repository
public class UserRepository implements IUserRepository {
    private final UserJpaDao userJpaDao;

    public UserRepository(UserJpaDao userJpaDao) {
        this.userJpaDao = userJpaDao;
    }

    @Override
    public List<User> findAll() {
        return userJpaDao.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public void save(User user) {
        UserJpaEntity userJpaEntity = UserMapper.toEntity(user);
        userJpaDao.save(userJpaEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaDao.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmailNormalized(String email) {
        return userJpaDao.findByEmailNormalized(email.toLowerCase().trim()).map(UserMapper::toDomain);
    }

}
