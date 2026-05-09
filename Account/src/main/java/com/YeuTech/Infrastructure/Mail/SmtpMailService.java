package com.YeuTech.Infrastructure.Mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.YeuTech.Application.Services.IMailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class SmtpMailService implements IMailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public SmtpMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendRegisterOtpEmail(String to, String otpCode, String language) throws Exception {
        String normalizedLanguage = normalizeLanguage(language);
        sendHtmlEmail(to, registerSubject(normalizedLanguage), buildRegisterContent(otpCode, normalizedLanguage));
    }

    @Override
    public void sendPasswordResetOtpEmail(String to, String otpCode, String language) throws Exception {
        String normalizedLanguage = normalizeLanguage(language);
        sendHtmlEmail(to, resetSubject(normalizedLanguage), buildResetContent(otpCode, normalizedLanguage));
    }

    @Override
    public void sendNotificationEmail(String to, String title, String message, String actionUrl, String language)
            throws Exception {
        String normalizedLanguage = normalizeLanguage(language);
        sendHtmlEmail(to, title, buildNotificationContent(message, actionUrl, normalizedLanguage));
    }

    private void sendHtmlEmail(String to, String subject, String html) throws Exception {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(msg);
    }

    private String normalizeLanguage(String language) {
        if (language == null || language.isBlank()) {
            return "en";
        }
        String normalized = language.trim().toLowerCase();
        return switch (normalized) {
            case "vi", "vn" -> "vi";
            case "jp", "ja" -> "jp";
            default -> "en";
        };
    }

    private String registerSubject(String language) {
        return switch (language) {
            case "vi" -> "Xac thuc tai khoan YeuTech";
            case "jp" -> "YeuTech \u30a2\u30ab\u30a6\u30f3\u30c8\u8a8d\u8a3c";
            default -> "Verify your YeuTech account";
        };
    }

    private String resetSubject(String language) {
        return switch (language) {
            case "vi" -> "Cai dat lai mat khau";
            case "jp" -> "\u30d1\u30b9\u30ef\u30fc\u30c9\u306e\u30ea\u30bb\u30c3\u30c8";
            default -> "Reset your password";
        };
    }

    private String buildRegisterContent(String otpCode, String language) {
        MailCopy copy = switch (language) {
            case "vi" -> new MailCopy(
                    "Xac thuc tai khoan",
                    "Cam on ban da dang ky tai khoan <strong>YeuTech</strong>.",
                    "Ma OTP cua ban la:",
                    "Ma co hieu luc trong <strong>5 phut</strong>. Vui long khong chia se ma nay voi bat ky ai.",
                    null);
            case "jp" -> new MailCopy(
                    "\u30a2\u30ab\u30a6\u30f3\u30c8\u8a8d\u8a3c",
                    "<strong>YeuTech</strong> \u306b\u3054\u767b\u9332\u3044\u305f\u3060\u304d\u3042\u308a\u304c\u3068\u3046\u3054\u3056\u3044\u307e\u3059\u3002",
                    "\u3042\u306a\u305f\u306eOTP\u30b3\u30fc\u30c9:",
                    "\u3053\u306e\u30b3\u30fc\u30c9\u306f <strong>5\u5206\u9593</strong> \u6709\u52b9\u3067\u3059\u3002\u8ab0\u306b\u3082\u5171\u6709\u3057\u306a\u3044\u3067\u304f\u3060\u3055\u3044\u3002",
                    null);
            default -> new MailCopy(
                    "Verify your account",
                    "Thank you for registering an account with <strong>YeuTech</strong>.",
                    "Your OTP code is:",
                    "This code is valid for <strong>5 minutes</strong>. Do not share it with anyone.",
                    null);
        };

        return buildOtpContent(copy, otpCode);
    }

    private String buildResetContent(String otpCode, String language) {
        MailCopy copy = switch (language) {
            case "vi" -> new MailCopy(
                    "Cai dat lai mat khau",
                    null,
                    "Ma OTP cua ban la:",
                    "Ma co hieu luc trong <strong>15 phut</strong>. Vui long khong chia se ma nay voi bat ky ai. Neu ban khong yeu cau cai dat lai mat khau, hay bo qua email nay.",
                    null);
            case "jp" -> new MailCopy(
                    "\u30d1\u30b9\u30ef\u30fc\u30c9\u306e\u30ea\u30bb\u30c3\u30c8",
                    null,
                    "\u3042\u306a\u305f\u306eOTP\u30b3\u30fc\u30c9:",
                    "\u3053\u306e\u30b3\u30fc\u30c9\u306f <strong>15\u5206\u9593</strong> \u6709\u52b9\u3067\u3059\u3002\u8ab0\u306b\u3082\u5171\u6709\u3057\u306a\u3044\u3067\u304f\u3060\u3055\u3044\u3002\u30d1\u30b9\u30ef\u30fc\u30c9\u306e\u30ea\u30bb\u30c3\u30c8\u3092\u30ea\u30af\u30a8\u30b9\u30c8\u3057\u3066\u3044\u306a\u3044\u5834\u5408\u306f\u3001\u3053\u306e\u30e1\u30fc\u30eb\u3092\u7121\u8996\u3057\u3066\u304f\u3060\u3055\u3044\u3002",
                    null);
            default -> new MailCopy(
                    "Reset your password",
                    null,
                    "Your OTP code is:",
                    "This code is valid for <strong>15 minutes</strong>. Do not share it with anyone. If you did not request a password reset, you can ignore this email.",
                    null);
        };

        return buildOtpContent(copy, otpCode);
    }

    private String buildOtpContent(MailCopy copy, String otpCode) {
        String intro = "";
        if (copy.intro() != null && !copy.intro().isBlank()) {
            intro = """
                    <p style="color: #555;">%s</p>
                    """.formatted(copy.intro());
        }

        return """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto; padding: 32px; border: 1px solid #e0e0e0; border-radius: 8px;">
                    <h2 style="color: #333;">%s</h2>
                    %s
                    <p style="color: #555;">%s</p>
                    <div style="font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #4F46E5; padding: 16px 0;">
                        %s
                    </div>
                    <p style="color: #999; font-size: 13px;">%s</p>
                </div>
                """.formatted(copy.heading(), intro, copy.otpLabel(), escapeHtml(otpCode), copy.footer());
    }

    private String buildNotificationContent(String message, String actionUrl, String language) {
        MailCopy copy = switch (language) {
            case "vi" -> new MailCopy(
                    "Thong bao moi",
                    null,
                    null,
                    null,
                    "Xem chi tiet");
            case "jp" -> new MailCopy(
                    "\u65b0\u3057\u3044\u901a\u77e5",
                    null,
                    null,
                    null,
                    "\u8a73\u7d30\u3092\u898b\u308b");
            default -> new MailCopy(
                    "New notification",
                    null,
                    null,
                    null,
                    "View details");
        };

        String actionBlock = "";
        if (actionUrl != null && !actionUrl.isBlank()) {
            actionBlock = """
                    <div style="margin-top: 20px;">
                        <a href="%s" style="display: inline-block; padding: 10px 16px; background: #2563eb; color: #fff; text-decoration: none; border-radius: 6px;">
                            %s
                        </a>
                    </div>
                    """.formatted(escapeHtml(actionUrl), copy.actionLabel());
        }

        return """
                <div style="font-family: Arial, sans-serif; max-width: 560px; margin: auto; padding: 24px; border: 1px solid #e0e0e0; border-radius: 8px;">
                    <h2 style="margin-bottom: 12px; color: #1f2937;">%s</h2>
                    <p style="line-height: 1.6; color: #374151;">%s</p>
                    %s
                </div>
                """.formatted(copy.heading(), escapeHtml(message == null ? "" : message), actionBlock);
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private record MailCopy(
            String heading,
            String intro,
            String otpLabel,
            String footer,
            String actionLabel) {
    }
}
