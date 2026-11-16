package com.example.lockedindungeonapi.controller;


import com.example.lockedindungeonapi.dto.appusage.AppUsageBragContent;
import com.example.lockedindungeonapi.dto.appusage.AppUsageBragRequest;
import com.example.lockedindungeonapi.dto.common.MessageResponse;
import com.example.lockedindungeonapi.dto.follow.FollowDto;
import com.example.lockedindungeonapi.dto.follow.FollowUserRequest;
import com.example.lockedindungeonapi.dto.follow.FriendListResponse;
import com.example.lockedindungeonapi.model.Follow;
import com.example.lockedindungeonapi.services.auth.UserInfoDetails;
import com.example.lockedindungeonapi.services.follow.FollowService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController{

    private FollowService service;


    public FollowController(FollowService service) {
        this.service = service;

    }

    @PostMapping("/follow")
    ResponseEntity<FollowDto> followUser(@AuthenticationPrincipal UserInfoDetails userInfo, @RequestBody FollowUserRequest req){
        Follow follow = this.service.follow(userInfo.getId(), req.userId());

        return ResponseEntity.status(201).body(FollowDto.fromFollow(follow));
    }

    @PostMapping("/unfollow")
    ResponseEntity<MessageResponse> unfollowUser(@AuthenticationPrincipal UserInfoDetails userInfo, @RequestBody FollowUserRequest req){
        this.service.unfollow(userInfo.getId(), req.userId());

        // 204 tidak boleh punya body, jadi message "Unfollow successful" memang tidak pernah kekirim
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/brag")
    ResponseEntity<MessageResponse> bragToFriend(@AuthenticationPrincipal UserInfoDetails user, @RequestBody AppUsageBragRequest req){
        var result = this.service.brag(user, req);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(result));
    }

    @GetMapping("/all")
    ResponseEntity<FriendListResponse> getFriendList(
            @AuthenticationPrincipal UserInfoDetails user,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size){
        FriendListResponse friendList = service.getFriendList(user.getId(), page, size);
        return ResponseEntity.status(200).body(friendList);
    }

    @GetMapping("/search")
    ResponseEntity<FriendListResponse> searchNewFriend(
            @AuthenticationPrincipal UserInfoDetails user,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "query", defaultValue = "") String query
    ){
        FriendListResponse result = service.searchNewFriend(user.getId(), query, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // hanya follower: yang follow kita tapi belum kita follow balik (mutual tidak ikut)
    @GetMapping("/followers")
    ResponseEntity<FriendListResponse> getFollowersOnly(
            @AuthenticationPrincipal UserInfoDetails user,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        FriendListResponse result = service.getFollowersOnly(user.getId(), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // hanya following: yang kita follow tapi belum follow balik (mutual tidak ikut)
    @GetMapping("/following")
    ResponseEntity<FriendListResponse> getFollowingOnly(
            @AuthenticationPrincipal UserInfoDetails user,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        FriendListResponse result = service.getFollowingOnly(user.getId(), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
