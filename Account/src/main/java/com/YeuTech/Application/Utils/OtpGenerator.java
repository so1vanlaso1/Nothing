package com.YeuTech.Application.Utils;

import java.security.SecureRandom;

public class OtpGenerator {
    public static String generateOtp(int length) {
        int code = new SecureRandom().nextInt(900_000) + 100_000;
        return String.valueOf(code).substring(0, length);
    }
}
