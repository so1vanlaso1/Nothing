package com.YeuTech.Infrastructure.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Entities.CustomDomain;
import com.YeuTech.Domain.Repository.ICustomDomainRepository;
import com.YeuTech.Infrastructure.Dao.CustomDomainJpaDao;
import com.YeuTech.Infrastructure.Mappers.CustomDomainMapper;

@Repository
public class CustomDomainRepositoryImpl implements ICustomDomainRepository {

    private final CustomDomainJpaDao customDomainJpaDao;

    public CustomDomainRepositoryImpl(CustomDomainJpaDao customDomainJpaDao) {
        this.customDomainJpaDao = customDomainJpaDao;
    }

    @Override
    public CustomDomain save(CustomDomain domain) {
        return CustomDomainMapper.toDomain(
                customDomainJpaDao.save(CustomDomainMapper.toEntity(domain)));
    }

    @Override
    public Optional<CustomDomain> findByDomainId(String domainId) {
        return customDomainJpaDao.findById(domainId)
                .map(CustomDomainMapper::toDomain);
    }

    @Override
    public Optional<CustomDomain> findByDomainName(String domainName) {
        return customDomainJpaDao.findByDomainName(domainName)
                .map(CustomDomainMapper::toDomain);
    }

    @Override
    public Optional<CustomDomain> findByDomainIdAndUserId(String domainId, String userId) {
        return customDomainJpaDao.findByDomainIdAndUserId(domainId, userId)
                .map(CustomDomainMapper::toDomain);
    }

    @Override
    public List<CustomDomain> findByUserId(String userId) {
        return customDomainJpaDao.findByUserIdOrderByCreatedDateDesc(userId).stream()
                .map(CustomDomainMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByDomainName(String domainName) {
        return customDomainJpaDao.existsByDomainName(domainName);
    }
}
