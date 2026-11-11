package com.example.lockedindungeonapi.services.auth;

import com.example.lockedindungeonapi.model.User;
import com.example.lockedindungeonapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {


    private final UserRepository userRepo;

    public UserInfoService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with username: " + username)
                );

        return new UserInfoDetails(user);
    }
}
