package com.example.lockedindungeonapi.controller;

import com.example.lockedindungeonapi.dto.common.MessageResponse;
import com.example.lockedindungeonapi.dto.user.RegisterFirebaseRequest;
import com.example.lockedindungeonapi.dto.user.UserDto;
import com.example.lockedindungeonapi.model.FirebaseEntity;
import com.example.lockedindungeonapi.services.auth.UserInfoDetails;
import com.example.lockedindungeonapi.services.user.UserService;
import com.google.api.gax.rpc.UnimplementedException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    public UserController(
        UserService service
    ){
        this.service = service;
    }

    @PostMapping("/registerFirebase")
    public ResponseEntity<FirebaseEntity> registerFirebaseEntity(@AuthenticationPrincipal UserInfoDetails user, @RequestBody RegisterFirebaseRequest request){
        FirebaseEntity entity = service.registerFirebase(user.getId(), request.fid(), request.deviceName());
        return ResponseEntity.status(201).body(entity);
    }

    @GetMapping("/all")
    public List<UserDto> getUsers(){
        return service.findAll().stream().map(UserDto::fromUser).toList();
    };

    @PostMapping("/testnotify")
    public  ResponseEntity<MessageResponse> testNotify(@AuthenticationPrincipal UserInfoDetails user, @RequestBody String  content){
        String fid = "sample-fid";

// See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("content", content)
                .putData("time", "2:45")
                .setNotification(Notification.builder().setBody(content).build())
                .setFid(fid)
                .build();

// Send a message to the device corresponding to the provided FID.
        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
            return ResponseEntity.status(200).body(new MessageResponse(response.toLowerCase()));
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }



}
