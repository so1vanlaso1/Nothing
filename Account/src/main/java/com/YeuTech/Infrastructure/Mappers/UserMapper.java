package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Infrastructure.Model.UserJpaEntity;

public class UserMapper {
    public static UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setActive(user.isActive());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setCreateDate(user.getCreatedDate());
        entity.setUpdateDate(user.getUpdatedDate());
        return entity;
    }

    public static User toDomain(UserJpaEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setActive(entity.isActive());
        user.setEmailVerified(entity.isEmailVerified());
        user.setCreatedDate(entity.getCreateDate());
        user.setUpdatedDate(entity.getUpdateDate());
        return user;
    }
}
