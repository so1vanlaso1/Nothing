package com.YeuTech.Infrastructure.Model;

import java.sql.Types;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cms_content_publications")
public class CmsContentPublicationJpaEntity {

    @Id
    @JdbcTypeCode(Types.CHAR)
    @Column(name = "publication_id", nullable = false, length = 36)
    private String publicationId;

    @JdbcTypeCode(Types.CHAR)
    @Column(name = "content_id", nullable = false, length = 36)
    private String contentId;

    @JdbcTypeCode(Types.CHAR)
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @JdbcTypeCode(Types.CHAR)
    @Column(name = "domain_id", nullable = false, length = 36)
    private String domainId;

    @Column(name = "slug", nullable = false, length = 255)
    private String slug;

    @Column(name = "canonical_url", nullable = false, length = 500)
    private String canonicalUrl;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
