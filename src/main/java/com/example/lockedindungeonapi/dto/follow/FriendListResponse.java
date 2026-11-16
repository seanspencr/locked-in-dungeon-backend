package com.example.lockedindungeonapi.dto.follow;

import com.example.lockedindungeonapi.dto.user.UserDto;

import java.util.List;

public record
FriendListResponse(
        List<UserDto> paginatedUser,
        Long totalFriends,
        Integer totalPages,
        Integer currentPage
) {
}
