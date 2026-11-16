package com.example.lockedindungeonapi.model;

import jakarta.persistence.*;
//import jakarta..*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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
    @JsonIgnore // hash password jangan pernah ikut ke response JSON
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();

    @Column(name="refresh_token", nullable = true)
    @JsonIgnore
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    @JsonIgnore // inverse side, kalau tidak di-ignore bikin cycle User -> FirebaseEntity -> User
    private Set<FirebaseEntity> firebaseEntities;

    @Column(name="refresh_token_issued", nullable = true)
    @JsonIgnore
    private Instant resfreshTokenIssued;


    @ColumnDefault("now()")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private Instant updatedAt;

    // optional = true: user lama bisa saja belum punya row user_settings, jadi jangan sampai
    // load User gagal (ObjectRetrievalFailureException) hanya karena settings-nya belum ada
    @OneToOne(mappedBy = "user", optional = true, cascade = CascadeType.ALL) //mapped by = tabel ini ndak pegang foreign keynya
    private UserSettings settings;


}
