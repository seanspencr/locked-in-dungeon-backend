package com.example.demo.controller;

import com.example.demo.dto.user.CreateUserRequest;
import com.example.demo.dto.user.CreateUserResponse;
import com.example.demo.dto.user.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody  CreateUserRequest req ){

        var res = this.service.createUser(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                res
        );
    }

    @GetMapping("/all")
    public List<UserDto> getUsers(){
        return service.findAll().stream().map(UserDto::fromUser).toList();
    };
}
