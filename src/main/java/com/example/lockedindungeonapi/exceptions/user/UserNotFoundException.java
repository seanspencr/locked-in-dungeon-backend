package com.example.lockedindungeonapi.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
