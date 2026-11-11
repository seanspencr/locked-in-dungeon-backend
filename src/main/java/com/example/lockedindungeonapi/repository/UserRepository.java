package com.example.lockedindungeonapi.repository;

import com.example.lockedindungeonapi.model.User;
import com.example.lockedindungeonapi.model.UserSettings;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // authorities ikut di-fetch, karena UserInfoDetails dibaca dari JwtAuthFilter (di luar session JPA)
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findByUsername(String username);
    
    Optional<User> findByRefreshToken(String refreshToken);


}
