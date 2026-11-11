package com.example.lockedindungeonapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    @GetMapping("/")
    public String helloWorld(){
        return "Hello world";
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "healthy";
    }
}
