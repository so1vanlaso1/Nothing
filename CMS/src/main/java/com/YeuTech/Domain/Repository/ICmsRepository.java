package com.YeuTech.Domain.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.YeuTech.Domain.Entities.CmsContent;

public interface ICmsRepository {
    List<CmsContent> findAll();

    CmsContent save(CmsContent content);

    Optional<CmsContent> findByContentIdAndUserId(String contentId, String userId);

        List<CmsContent> findRecentByUserId(String userId, int limit);

    Page<CmsContent> findByUserIdWithFilters(
            String userId,
            String status,
            String contentType,
            String platform,
            Boolean hasGeneration,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String search,
            Pageable pageable);

    void delete(CmsContent content);

    boolean existsByContentIdAndUserId(String contentId, String userId);

    Optional<CmsContent> findByContentIdAndUserIdAndStatus(String contentId, String userId, String status);

    long countByUserIdAndCreateDateBetween(String userId, LocalDateTime startInclusive, LocalDateTime endExclusive);

    long countByUserIdAndStatusAndCreateDateBetween(
            String userId,
            String status,
            LocalDateTime startInclusive,
            LocalDateTime endExclusive);
}
