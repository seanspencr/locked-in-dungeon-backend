package com.example.lockedindungeonapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "authorities")
@Getter(AccessLevel.PUBLIC)
@Setter
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id", nullable = false, unique = true)
    private Long id;

    @Size(max = 255) //ini constrain programmatical
    @NotNull()
    @Column(name = "name", nullable = false, length = 255, unique = true) // ini constraint yang ketanem di DB
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();

    public static Authority ofUser(){
        Authority auth =  new Authority();
        auth.setName("USER");
        return auth;
    }

    public static Authority ofAdmin(){
        Authority auth =  new Authority();
        auth.setName("ADMIN");
        return auth;
    }

}
