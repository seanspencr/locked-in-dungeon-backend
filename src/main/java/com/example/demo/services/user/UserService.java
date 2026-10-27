package com.example.demo.services.user;

import com.example.demo.dto.auth.SignupRequest;
import com.example.demo.dto.auth.SignupResponse;
import com.example.demo.dto.user.UserDto;
import com.example.demo.exceptions.user.UserAlreadyExistException;
import com.example.demo.exceptions.user.UserNotFoundException;
import com.example.demo.model.Authority;
import com.example.demo.model.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.*;

@Service
public class UserService  {

    UserRepository userRepo;
    PasswordEncoder encoder;
    AuthorityRepository authorityRepository;
    public UserService(UserRepository userRepo, AuthorityRepository authorityRepository, PasswordEncoder encoder){
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.authorityRepository = authorityRepository;
    }



    public User findUserByUsername(String username){
        var user = userRepo.findByUsername(username);
        return user.orElseThrow(() -> new UserNotFoundException("User is not found"));
    }

    private Authority findOrCreateAuthority(String name){
        return authorityRepository.findByName(name).orElseGet(
                ()-> {
                    Authority newAuth = new Authority();
                    newAuth.setName(name);
                    Authority saved = authorityRepository.save(newAuth);
                    return  saved;
                }
        );
    }

    public SignupResponse createUser(SignupRequest req) {

        var existing = userRepo.findByUsername(req.username());
        if(existing.isPresent()){
          throw new UserAlreadyExistException("User already exist");
        }

//        user.setPassword(req.password());

        User user = new User();
        user.setPassword(encoder.encode(req.password()));
        user.setUsername(req.username());
        Set<Authority> auth = new HashSet<>();
        auth.add(findOrCreateAuthority(Authority.ofUser().getName()));
        user.setAuthorities(auth);

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


    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepo.findByRefreshToken(refreshToken);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }
}
