package com.example.demo.services.user;

import com.example.demo.dto.auth.SignupRequest;
import com.example.demo.dto.auth.SignupResponse;
import com.example.demo.dto.user.UserDto;
import com.example.demo.exceptions.auth.RefreshTokenNotFoundException;
import com.example.demo.exceptions.user.UserAlreadyExistException;
import com.example.demo.exceptions.user.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService  {

    UserRepository userRepo;
    PasswordEncoder encoder;
    public UserService(UserRepository userRepo, PasswordEncoder encoder){
        this.userRepo = userRepo;
        this.encoder = encoder;
    }



    public User findUserByUsername(String username){
        var user = userRepo.findByUsername(username);
        return user.orElseThrow(() -> new UserNotFoundException("User is not found"));
    }

    public SignupResponse createUser(SignupRequest req) {

        var existing = userRepo.findByUsername(req.username());
        if(existing.isPresent()){
          throw new UserAlreadyExistException("User already exist");
        }

        User user = new User();
        user.setPassword(encoder.encode(req.password()));
//        user.setPassword(req.password());
        user.setUsername(req.username());
        user.setRoles("USER");

        try {
            var inserted = userRepo.save(user);
            return new SignupResponse(
                    UserDto.fromUser(inserted),
                    "Success"
            );
        } catch (TransactionSystemException e) {
            Throwable rootCause = e.getRootCause();
            // Cek apakah rootCause adalah ConstraintViolationException (validasi)
            if (rootCause instanceof ConstraintViolationException) {
                // Tampilkan pesan validasi yang gagal
            }
            // Atau cek pesan error database lainnya
            throw( new RuntimeException(Arrays.toString(e.getStackTrace())));
        }


    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User refreshUserToken(String refreshToken) {
        User user = userRepo.findByRefreshToken(refreshToken).orElseThrow(()->new RefreshTokenNotFoundException("Refresh token is invalid, please relogin"));

//        TODO : validasi usia refresh token

//        TODO : rotate refresh token
        user.setResfreshTokenIssued(Instant.now());
        userRepo.save(user);
        return user;
    }
}
