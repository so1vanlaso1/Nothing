package com.YeuTech.Domain.Repository;

import java.util.List;
import java.util.Optional;

import com.YeuTech.Domain.Entities.CustomDomain;

public interface ICustomDomainRepository {
    CustomDomain save(CustomDomain domain);

    Optional<CustomDomain> findByDomainId(String domainId);

    Optional<CustomDomain> findByDomainName(String domainName);

    Optional<CustomDomain> findByDomainIdAndUserId(String domainId, String userId);

    List<CustomDomain> findByUserId(String userId);

    boolean existsByDomainName(String domainName);
}
