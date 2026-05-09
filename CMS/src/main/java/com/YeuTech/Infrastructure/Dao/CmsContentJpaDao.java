package com.YeuTech.Infrastructure.Dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.CmsContentJpaEntity;

@Repository
public interface CmsContentJpaDao extends JpaRepository<CmsContentJpaEntity, String> {

    Optional<CmsContentJpaEntity> findByContentIdAndUserId(String contentId, String userId);

    Optional<CmsContentJpaEntity> findByContentIdAndUserIdAndStatus(String contentId, String userId, String status);

    boolean existsByContentIdAndUserId(String contentId, String userId);

     List<CmsContentJpaEntity> findByUserIdOrderByCreateDateDesc(String userId, Pageable pageable);

    long countByUserIdAndCreateDateGreaterThanEqualAndCreateDateLessThan(
         String userId,
         LocalDateTime startInclusive,
         LocalDateTime endExclusive);

    long countByUserIdAndStatusAndCreateDateGreaterThanEqualAndCreateDateLessThan(
         String userId,
         String status,
         LocalDateTime startInclusive,
         LocalDateTime endExclusive);

    @Query("""
            SELECT c
            FROM CmsContentJpaEntity c
            WHERE c.userId = :userId
              AND (:status IS NULL OR c.status = :status)
              AND (:contentType IS NULL OR c.contentType = :contentType)
              AND (:platform IS NULL OR c.platform = :platform)
              AND (
                   :hasGeneration IS NULL
                   OR (:hasGeneration = TRUE AND c.generationId IS NOT NULL)
                   OR (:hasGeneration = FALSE AND c.generationId IS NULL)
              )
              AND (:fromDate IS NULL OR c.createDate >= :fromDate)
              AND (:toDate IS NULL OR c.createDate <= :toDate)
              AND (
                   :search IS NULL
                   OR LOWER(COALESCE(c.title, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(COALESCE(c.contentBody, '')) LIKE LOWER(CONCAT('%', :search, '%'))
              )
            """)
    Page<CmsContentJpaEntity> findByUserIdWithFilters(
            @Param("userId") String userId,
            @Param("status") String status,
            @Param("contentType") String contentType,
            @Param("platform") String platform,
            @Param("hasGeneration") Boolean hasGeneration,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("search") String search,
            Pageable pageable);
}
