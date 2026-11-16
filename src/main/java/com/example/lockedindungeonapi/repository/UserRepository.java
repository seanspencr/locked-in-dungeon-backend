package com.example.lockedindungeonapi.repository;

import com.example.lockedindungeonapi.model.User;
import com.example.lockedindungeonapi.model.UserSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // authorities ikut di-fetch, karena UserInfoDetails dibaca dari JwtAuthFilter (di luar session JPA)
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findByUsername(String username);
    
    Optional<User> findByRefreshToken(String refreshToken);

    // cari user yang belum punya entry apapun di tabel friends dengan user yang lagi login
    // (belum follow dan belum di-follow), exclude diri sendiri
    @Query(
            value = """
            SELECT u FROM User u
            WHERE u.id <> :userId
            AND LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
            AND NOT EXISTS (
                SELECT 1 FROM Follow f
                WHERE (f.follower.id = :userId AND f.target.id = u.id)
                OR (f.follower.id = u.id AND f.target.id = :userId)
            )
        """,
            countQuery = """
            SELECT COUNT(u) FROM User u
            WHERE u.id <> :userId
            AND LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
            AND NOT EXISTS (
                SELECT 1 FROM Follow f
                WHERE (f.follower.id = :userId AND f.target.id = u.id)
                OR (f.follower.id = u.id AND f.target.id = :userId)
            )
        """
    )
    Page<User> searchUsersWithoutFollow(@Param("userId") Long userId, @Param("query") String query, Pageable pageable);


}
