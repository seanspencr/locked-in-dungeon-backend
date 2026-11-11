package com.example.lockedindungeonapi.services.follow;

import com.example.lockedindungeonapi.dto.follow.FriendListResponse;
import com.example.lockedindungeonapi.model.Follow;
import com.example.lockedindungeonapi.model.User;
import com.example.lockedindungeonapi.repository.FollowRepository;
import com.example.lockedindungeonapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FollowService {

    private FollowRepository repo;
    private UserRepository userRepository;

    public FollowService(FollowRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
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
        List<User> friendList = friendsPage.getContent();

        return new FriendListResponse(friendList, friendsPage.getTotalElements(), friendsPage.getTotalPages(), friendsPage.getNumber());
    }

    public void unfollow(Long followerId, Long targetId) {
        Follow follow = new Follow();

//        get reference by id ini supaya gaperlu jalanin query select
        follow.setFollower(userRepository.getReferenceById(followerId));
        follow.setTarget(userRepository.getReferenceById(targetId));

        repo.delete(follow);
    }
}
