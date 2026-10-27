package com.example.demo.services.auth;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.exceptions.auth.RefreshTokenNotFoundException;
import com.example.demo.exceptions.user.UserNotFoundException;
import com.example.demo.exceptions.user.UserWrongCredentialException;
import com.example.demo.model.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.services.user.UserService;
import com.example.demo.util.RefreshTokenGenerator;
import com.example.demo.util.SHA256Hasher;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private UserService userService;

    public AuthService(UserService userService
    ){
        this.userService = userService;
    }


    public User refreshUserToken(String refreshToken) {
        User user = userService.findByRefreshToken(refreshToken).orElseThrow(()->new RefreshTokenNotFoundException("Refresh token is invalid, please relogin"));

//        TODO : validasi usia refresh token

//        rotate refresh token
        String newRefreshToken = RefreshTokenGenerator.generate();
        user.setRefreshToken(SHA256Hasher.hash(newRefreshToken));
        user.setResfreshTokenIssued(Instant.now());

        userService.saveUser(user);
        return user;
    }

    public User refreshUserTokenByUsername(String username){
        User user = userService.findUserByUsername(username);

//        TODO : validasi usia refresh token

//        rotate refresh token
        String newRefreshToken = RefreshTokenGenerator.generate();
        user.setRefreshToken(SHA256Hasher.hash(newRefreshToken));
        user.setResfreshTokenIssued(Instant.now());

        userService.saveUser(user);
        return user;
    }
}
