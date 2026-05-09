package com.YeuTech.Infrastructure.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.YeuTech.Infrastructure.Model.UserProfileJpaEntity;

public interface UserProfileJpaDao extends JpaRepository<UserProfileJpaEntity, String> {
}
