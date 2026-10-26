package com.example.demo.exceptions.user;

public class UserWrongCredentialException extends RuntimeException {
    public UserWrongCredentialException(String message) {
        super(message);
    }
}
