package com.example.lockedindungeonapi.exceptions.auth;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
