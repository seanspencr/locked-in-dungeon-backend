package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserRepository extends JpaRepository<User, Long> {

    // authorities ikut di-fetch, karena UserInfoDetails dibaca dari JwtAuthFilter (di luar session JPA)
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findByUsername(String username);
    
    Optional<User> findByRefreshToken(String refreshToken);
}
