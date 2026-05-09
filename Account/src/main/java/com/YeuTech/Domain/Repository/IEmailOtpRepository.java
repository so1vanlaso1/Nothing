package com.YeuTech.Domain.Repository;

import com.YeuTech.Domain.Entities.EmailOtp;

import java.util.Optional;

public interface IEmailOtpRepository {
    void save(EmailOtp emailOtp);
    Optional<EmailOtp> findActiveByUserId(String userId);

    void deleteByUserId(String userId);
}
