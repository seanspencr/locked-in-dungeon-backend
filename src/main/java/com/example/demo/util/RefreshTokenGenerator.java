package com.example.demo.util;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class RefreshTokenGenerator {
    private static SecureRandom secureRandom = new SecureRandom();
    private static Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generate() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
