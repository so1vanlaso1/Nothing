package com.YeuTech.Infrastructure.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.CustomDomainJpaEntity;

@Repository
public interface CustomDomainJpaDao extends JpaRepository<CustomDomainJpaEntity, String> {

    Optional<CustomDomainJpaEntity> findByDomainName(String domainName);

    Optional<CustomDomainJpaEntity> findByDomainIdAndUserId(String domainId, String userId);

    List<CustomDomainJpaEntity> findByUserIdOrderByCreatedDateDesc(String userId);

    boolean existsByDomainName(String domainName);

    Optional<CustomDomainJpaEntity> findByDomainNameAndIsActiveTrue(String domainName);
}
