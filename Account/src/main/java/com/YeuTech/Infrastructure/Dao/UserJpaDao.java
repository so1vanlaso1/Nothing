package com.YeuTech.Infrastructure.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.UserJpaEntity;

@Repository
public interface UserJpaDao extends JpaRepository<UserJpaEntity, String> {
    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByEmailNormalized(String emailNormalized);
}
