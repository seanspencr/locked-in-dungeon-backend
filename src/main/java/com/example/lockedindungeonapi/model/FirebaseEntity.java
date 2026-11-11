package com.example.lockedindungeonapi.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "firebase_entity")
@Getter(AccessLevel.PUBLIC)
@Setter
public class FirebaseEntity {
    @Id
    @Column(name = "fid", unique = true, nullable = false)
    private String fid;

    @Column(name = "device_name", nullable = true)
    private String deviceName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
