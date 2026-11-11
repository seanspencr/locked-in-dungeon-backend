package com.example.lockedindungeonapi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserSettings {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId //supaya mapping idenya ke userId
    @JoinColumn(name = "user_id") //joincolumn = table ini yang pegang foreign key
    @JsonIgnore
    private User user;

    @Column(name = "show_stats")
    public boolean showStats = true;
    @Column(name = "show_block_list")
    public boolean showBlocklist = true;



    public UserSettings( User user) {
        this.user = user;
    }

    public UserSettings() {
    }
}
