package com.YeuTech.Infrastructure.Repository;

import com.YeuTech.Domain.Entities.EmailOtp;
import com.YeuTech.Domain.Repository.IEmailOtpRepository;
import com.YeuTech.Infrastructure.Dao.EmailOtpJpaDao;
import com.YeuTech.Infrastructure.Mappers.EmailOtpMapper;
import com.YeuTech.Infrastructure.Model.EmailOtpJpaEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class EmailOtpRepository implements IEmailOtpRepository {
    private final EmailOtpJpaDao emailOtpJpaDao;

    public EmailOtpRepository(EmailOtpJpaDao emailOtpJpaDao) {
        this.emailOtpJpaDao = emailOtpJpaDao;
    }
    @Override
    public void save(EmailOtp emailOtp) {
        EmailOtpJpaEntity emailOtpJpaEntity = EmailOtpMapper.toEntity(emailOtp);
        emailOtpJpaDao.save(emailOtpJpaEntity);
    }

    @Override
    public Optional<EmailOtp> findActiveByUserId(String userId) {
        Optional<EmailOtpJpaEntity> emailOtpJpaEntityOpt = emailOtpJpaDao.findByUserIdAndIsUsedFalse(userId);
        return emailOtpJpaEntityOpt.map(EmailOtpMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByUserId(String userId) {
        emailOtpJpaDao.deleteByUserId(userId);
    }
}
