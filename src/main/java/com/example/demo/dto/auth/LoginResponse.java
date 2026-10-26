package com.example.demo.dto.auth;

public record LoginResponse(
        String message,
        String accessToken,
        String refreshToken
        ) {
}
