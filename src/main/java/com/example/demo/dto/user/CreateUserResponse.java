package com.example.demo.dto.user;

import com.example.demo.model.User;


public record CreateUserResponse(
    UserDto user,
    String message
) {
}
