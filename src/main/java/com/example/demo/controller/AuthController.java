package com.example.demo.controller;

import com.example.demo.dto.auth.*;
import com.example.demo.exceptions.user.UserWrongCredentialException;
import com.example.demo.model.Authority;
import com.example.demo.model.User;
import com.example.demo.services.auth.AuthService;
import com.example.demo.services.auth.JwtService;
import com.example.demo.util.RefreshTokenGenerator;
import com.example.demo.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;
    UserService userService;
    RefreshTokenGenerator refreshTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService service, RefreshTokenGenerator refreshTokenService, UserService userService, JwtService jwtService, AuthenticationManager authenticationManager){
        this.authService = service;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
//        var res = service.login(request);
//        return ResponseEntity.status(HttpStatus.OK).body(res);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        if (!authentication.isAuthenticated()) {
            throw new UserWrongCredentialException("Invalid login!");
        }


        var user = authService.refreshUserTokenByUsername(request.username());
        String accessToken = this.jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.OK).body(
            new LoginResponse("Success", accessToken , user.getRefreshToken())
        );

    }


    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> createUser(@RequestBody SignupRequest req ){

        var res = this.userService.createUser(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                res
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserDetails user){
//        gaada logic lagi karena smua checking udah dihandle difliter, request masuk sini brarti uda authenticated

        List<String> authorityList = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.status(HttpStatus.OK).body(
                new MeResponse(
                        user.getUsername(),
                        authorityList
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest req){
        User user = authService.refreshUserToken(req.refreshToken());
        String accessToken = jwtService.generateToken(user);
//        TODO : generate new refresh token
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse("Success", accessToken, user.getRefreshToken()));
    }

}

