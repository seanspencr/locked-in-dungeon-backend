package com.example.lockedindungeonapi.repository;

import com.example.lockedindungeonapi.model.Follow;
import com.example.lockedindungeonapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {


    List<Follow> findByFollower(User follower);

    // 2. Get all followers of a specific user (user is user2)
    List<Follow> findByTarget(User target);

    // 3. Check if user1 is already following user2
    boolean existsByFollowerAndTarget(User follower, User target);

    // 4. Delete follow relationship
    void deleteByFollowerAndTarget(User follower, User target);

//    5. query mutual
    @Query(
            value = """
            SELECT f1.target FROM Follow f1
            WHERE f1.follower = :user
            AND EXISTS (
                SELECT 1 FROM Follow f2
                WHERE f2.follower = f1.target AND f2.target = f1.follower
            )
        """,
            countQuery = """
            SELECT COUNT(f1) FROM Follow f1
            WHERE f1.follower = :user
            AND EXISTS (
                SELECT 1 FROM Follow f2
                WHERE f2.follower = f1.target AND f2.target = f1.follower
            )
        """
    )
    Page<User> findMutualFriends(@Param("user") User user, Pageable pageable);

    //    6. hanya follower: user yang follow kita tapi belum kita follow balik (exclude mutual)
    @Query(
            value = """
            SELECT f1.follower FROM Follow f1
            WHERE f1.target = :user
            AND NOT EXISTS (
                SELECT 1 FROM Follow f2
                WHERE f2.follower = :user AND f2.target = f1.follower
            )
            ORDER BY f1.follower.username ASC
        """,
            countQuery = """
            SELECT COUNT(f1) FROM Follow f1
            WHERE f1.target = :user
            AND NOT EXISTS (
                SELECT 1 FROM Follow f2
                WHERE f2.follower = :user AND f2.target = f1.follower
            )
        """
    )
    Page<User> findFollowersOnly(@Param("user") User user, Pageable pageable);

    //    7. hanya following: user yang kita follow tapi belum follow balik (exclude mutual)
    @Query(
            value = """
            SELECT f1.target FROM Follow f1
            WHERE f1.follower = :user
            AND NOT EXISTS (
                SELECT 1 FROM Follow f2
                WHERE f2.follower = f1.target AND f2.target = :user
            )
            ORDER BY f1.target.username ASC
        """,
            countQuery = """
            SELECT COUNT(f1) FROM Follow f1
            WHERE f1.follower = :user
            AND NOT EXISTS (
                SELECT 1 FROM Follow f2
                WHERE f2.follower = f1.target AND f2.target = :user
            )
        """
    )
    Page<User> findFollowingOnly(@Param("user") User user, Pageable pageable);
}
