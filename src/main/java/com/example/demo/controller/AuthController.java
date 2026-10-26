package com.example.demo.controller;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.exceptions.user.UserWrongCredentialException;
import com.example.demo.services.AuthService;
import com.example.demo.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService service, JwtService jwtService, AuthenticationManager authenticationManager){
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
//        var res = service.login(request);
//        return ResponseEntity.status(HttpStatus.OK).body(res);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.pasword())
        );
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(request.username());
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse("Success", accessToken, "refreshToken"));
        } else {
            throw new UserWrongCredentialException("Invalid login!");
        }
    }

}

