package com.example.lockedindungeonapi.dto.follow;

import com.example.lockedindungeonapi.model.Follow;
import com.example.lockedindungeonapi.model.User;

import java.util.List;

public record FriendListResponse(
        List<User> paginatedUser,
        Long totalFriends,
        Integer totalPages,
        Integer currentPage
) {
}
