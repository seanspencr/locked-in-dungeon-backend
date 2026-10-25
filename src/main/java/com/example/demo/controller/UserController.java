package com.example.demo.controller;

import com.example.demo.dto.user.CreateUserRequest;
import com.example.demo.dto.user.CreateUserResponse;
import com.example.demo.dto.user.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepo;
    public UserController(
        UserRepository userRepo
    ){
        this.userRepo = userRepo;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody  CreateUserRequest req ){

        User user = new User();
        user.setPassword(req.password());
        user.setUsername(req.username());

        var inserted = userRepo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreateUserResponse(UserDto.fromUser(inserted),  "Succes")
        );
    }

    @GetMapping("/all")
    public List<UserDto> getUsers(){
        return userRepo.findAll().stream().map(UserDto::fromUser).toList();
    };
}
