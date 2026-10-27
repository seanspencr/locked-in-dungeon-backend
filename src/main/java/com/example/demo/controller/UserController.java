package com.example.demo.controller;

import com.example.demo.dto.user.UserDto;
import com.example.demo.services.user.UserService;
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

    @GetMapping("/all")
    public List<UserDto> getUsers(){
        return service.findAll().stream().map(UserDto::fromUser).toList();
    };
}
