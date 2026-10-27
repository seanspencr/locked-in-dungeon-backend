package com.example.demo.dto.auth;


import com.example.demo.dto.user.UserDto;

public record SignupResponse(
    UserDto user,
    String message
) {
}
