package com.example.lockedindungeonapi.dto.auth;

public record LoginResponse(
        String message,
        String accessToken,
        String refreshToken
        ) {
}
