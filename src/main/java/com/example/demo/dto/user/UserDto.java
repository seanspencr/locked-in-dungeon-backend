package com.example.demo.dto.user;

import com.example.demo.model.User;

import java.time.Instant;

public record UserDto(
        Long id,
        String username,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserDto fromUser(User user){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}