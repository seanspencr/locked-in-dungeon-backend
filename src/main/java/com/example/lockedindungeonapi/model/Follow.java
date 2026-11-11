package com.example.lockedindungeonapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "friends", uniqueConstraints = @UniqueConstraint(columnNames = {"user_1_id", "user_2_id"}))
@Setter
@Getter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_1_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "user_2_id", nullable = false)
    private User target;
}
