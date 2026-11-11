package com.example.lockedindungeonapi.controller;


import com.example.lockedindungeonapi.dto.common.MessageResponse;
import com.example.lockedindungeonapi.dto.follow.FollowUserRequest;
import com.example.lockedindungeonapi.dto.follow.FriendListResponse;
import com.example.lockedindungeonapi.model.Follow;
import com.example.lockedindungeonapi.services.auth.UserInfoDetails;
import com.example.lockedindungeonapi.services.follow.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController{

    private FollowService service;

    public FollowController(FollowService service) {
        this.service = service;
    }

    @PostMapping("/follow")
    ResponseEntity<Follow> followUser(@AuthenticationPrincipal UserInfoDetails userInfo, @RequestBody FollowUserRequest req){
        Follow follow = this.service.follow(userInfo.getId(), req.userId());

        return ResponseEntity.status(201).body(follow);
    }

    @PostMapping("/unfollow")
    ResponseEntity<MessageResponse> unfollowUser(@AuthenticationPrincipal UserInfoDetails userInfo, @RequestBody FollowUserRequest req){
        this.service.unfollow(userInfo.getId(), req.userId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("Unfollow successful"));
    }

    @GetMapping("/all")
    ResponseEntity<FriendListResponse> getFriendList(
            @AuthenticationPrincipal UserInfoDetails user,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size){
        FriendListResponse friendList = service.getFriendList(user.getId(), page, size);
        return ResponseEntity.status(200).body(friendList);
    }

}
