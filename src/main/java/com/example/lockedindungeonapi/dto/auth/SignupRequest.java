package com.example.lockedindungeonapi.dto.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "Username cannot be empty")
        @Size(max = 32, message = "Username must not exceed 32 characters")
        String username,

        @NotBlank(message = "Password cannot be empty")
        @Size(max = 32, message = "Password must not exceed 32 characters")
        String password
) {}
