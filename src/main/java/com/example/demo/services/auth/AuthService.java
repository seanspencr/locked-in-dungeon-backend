package com.example.demo.services.auth;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.exceptions.user.UserNotFoundException;
import com.example.demo.exceptions.user.UserWrongCredentialException;
import com.example.demo.services.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserService userService;
    public AuthService(UserService userService){
        this.userService = userService;
    }

    public LoginResponse login(LoginRequest req){
        try {
            var user = this.userService.findUserByUsername(req.username());
            if(!user.getPassword().equals(req.pasword())){
                throw new UserWrongCredentialException("User not found, please make recheck username and password");
            }

            return new LoginResponse(
                    "Success",
                    "accessToken",
                    "refreshToken"
            );
        }catch (UserNotFoundException e){
            throw new UserWrongCredentialException("User not found, please make recheck username and password");
        }
    }
}
