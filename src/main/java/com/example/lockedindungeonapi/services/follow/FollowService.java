package com.example.lockedindungeonapi.services.follow;

import com.example.lockedindungeonapi.dto.appusage.AppUsageBragContent;
import com.example.lockedindungeonapi.dto.appusage.AppUsageBragRequest;
import com.example.lockedindungeonapi.dto.follow.FriendListResponse;
import com.example.lockedindungeonapi.dto.user.UserDto;
import com.example.lockedindungeonapi.exceptions.user.UserNotFoundException;
import com.example.lockedindungeonapi.model.Follow;
import com.example.lockedindungeonapi.model.User;
import com.example.lockedindungeonapi.repository.FollowRepository;
import com.example.lockedindungeonapi.repository.UserRepository;
import com.example.lockedindungeonapi.services.auth.UserInfoDetails;
import com.example.lockedindungeonapi.services.common.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;


@Service
public class FollowService {

    private FollowRepository repo;
    private UserRepository userRepository;
    private NotificationService notifService;

    private ObjectMapper objectMapper;


    public FollowService(FollowRepository repo, UserRepository userRepository, NotificationService notifService, ObjectMapper objectMapper) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.notifService = notifService;
        this.objectMapper = objectMapper;
    }

    public Follow follow(Long followerId, Long targetId){
        Follow follow = new Follow();

//        get reference by id ini supaya gaperlu jalanin query select
        follow.setFollower(userRepository.getReferenceById(followerId));
        follow.setTarget(userRepository.getReferenceById(targetId));

        return repo.save(follow);
    }


    public FriendListResponse getFriendList(Long userId, int pageNum, int pageSize) {
        var user = userRepository.getReferenceById(userId);
        Page<User> friendsPage = repo.findMutualFriends(user, PageRequest.of(pageNum, pageSize));

        return toFriendListResponse(friendsPage);
    }

    public FriendListResponse getFollowersOnly(Long userId, int pageNum, int pageSize) {
        var user = userRepository.getReferenceById(userId);
        Page<User> followersPage = repo.findFollowersOnly(user, PageRequest.of(pageNum, pageSize));

        return toFriendListResponse(followersPage);
    }

    public FriendListResponse getFollowingOnly(Long userId, int pageNum, int pageSize) {
        var user = userRepository.getReferenceById(userId);
        Page<User> followingPage = repo.findFollowingOnly(user, PageRequest.of(pageNum, pageSize));

        return toFriendListResponse(followingPage);
    }

    public FriendListResponse searchNewFriend(Long userId, String query, int pageNum, int pageSize) {
        String keyword = query == null ? "" : query.trim();
        Page<User> usersPage = userRepository.searchUsersWithoutFollow(
                userId,
                keyword,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "username"))
        );

        return toFriendListResponse(usersPage);
    }

    private FriendListResponse toFriendListResponse(Page<User> usersPage) {
        List<UserDto> userList = usersPage.getContent().stream().map(UserDto::fromUser).toList();

        return new FriendListResponse(userList, usersPage.getTotalElements(), usersPage.getTotalPages(), usersPage.getNumber());
    }

    public void unfollow(Long followerId, Long targetId) {
        Follow follow = new Follow();

//        get reference by id ini supaya gaperlu jalanin query select
        follow.setFollower(userRepository.getReferenceById(followerId));
        follow.setTarget(userRepository.getReferenceById(targetId));

        repo.delete(follow);
    }

    public String brag(UserInfoDetails user, AppUsageBragRequest req) {
        var userId = req.targetUserid();
        User target = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("Failed to brag, user not found"));


        var targetFirebaseEntities = target.getFirebaseEntities();
        for(var firebaseEntity : targetFirebaseEntities){
            var fid = firebaseEntity.getFid();

            try {
                String result = notifService.sendNotification(
                        fid,
                        "Message from " + user.getUsername(),
                        req.content().message().orElse(""),
                            req.content().toFcmDataMap(objectMapper)
                        );
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return "Success";

    }
}
