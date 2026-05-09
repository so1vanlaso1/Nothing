package com.YeuTech.Infrastructure.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.CmsUserJpaEntity;

@Repository
public interface CmsUserJpaDao extends JpaRepository<CmsUserJpaEntity, String> {
    Optional<CmsUserJpaEntity> findByEmailNormalized(String emailNormalized);
}
