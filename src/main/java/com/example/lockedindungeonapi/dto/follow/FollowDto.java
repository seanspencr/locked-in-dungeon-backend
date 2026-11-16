package com.example.lockedindungeonapi.dto.follow;

import com.example.lockedindungeonapi.dto.user.UserDto;
import com.example.lockedindungeonapi.model.Follow;
import org.springframework.security.core.userdetails.UserDetails;

public record FollowDto(
    UserDto follower,
    UserDto target
) {
    public static FollowDto fromFollow(Follow follow){
        return new FollowDto( UserDto.fromUser(follow.getFollower()), UserDto.fromUser(follow.getTarget()));
    }
}
