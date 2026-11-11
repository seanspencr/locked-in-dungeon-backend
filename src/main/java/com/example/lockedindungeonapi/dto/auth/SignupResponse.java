package com.example.lockedindungeonapi.dto.auth;


import com.example.lockedindungeonapi.dto.user.UserDto;

public record SignupResponse(
    UserDto user,
    String message
) {
}
