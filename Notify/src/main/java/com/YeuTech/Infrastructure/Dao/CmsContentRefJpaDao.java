package com.YeuTech.Infrastructure.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.CmsContentJpaEntity;

@Repository
public interface CmsContentRefJpaDao extends JpaRepository<CmsContentJpaEntity, String> {
    boolean existsByContentIdAndUserId(String contentId, String userId);
}
