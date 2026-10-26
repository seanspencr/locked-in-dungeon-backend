package com.example.demo.model;

import jakarta.persistence.*;
//import jakarta..*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

//@Setter(AccessLevel.PRIVATE)
//@Getter(AccessLevel.PUBLIC)

@Entity
@Table(name = "users")
@Getter(AccessLevel.PUBLIC)
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq")
    @Column(name ="id", nullable = false, unique = true)
    private Long id;

    @Size(max = 255) //ini constrain programmatical
    @NotNull()
    @Column(name = "username", nullable = false, length = 255, unique = true) // ini constraint yang ketanem di DB
    private String username;

    @Size(max = 255) //ini constrain programmatical
    @NotNull()
    @Column(name = "password", nullable = false, length = 255) // ini constraint yang ketanem di DB
    private String password;

    @ColumnDefault("USER")
    @Column(name = "roles", nullable = false) // ini constraint yang ketanem di DB
    private String roles;

    @ColumnDefault("now()")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private Instant updatedAt;

}
