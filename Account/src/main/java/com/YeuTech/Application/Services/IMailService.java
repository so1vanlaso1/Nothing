package com.YeuTech.Application.Services;

public interface IMailService {
    void sendRegisterOtpEmail(String to, String otpCode, String language) throws Exception;

    void sendPasswordResetOtpEmail(String to, String otpCode, String language) throws Exception;

    void sendNotificationEmail(String to, String title, String message, String actionUrl, String language) throws Exception;
}
