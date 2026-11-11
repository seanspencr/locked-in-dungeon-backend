package com.example.lockedindungeonapi.model;


import jakarta.persistence.*;

@Entity
public class UserSettings {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId //supaya mapping idenya ke userId
    @JoinColumn(name = "user_id") //joincolumn = table ini yang pegang foreign key
    private User user;

    @Column(name = "show_stats")
    public boolean showStats = true;
    @Column(name = "show_block_list")
    public boolean showBlocklist = true;



    public UserSettings(boolean showStats, boolean showBlocklist) {
        this.showStats = showStats;
        this.showBlocklist = showBlocklist;
    }

    public UserSettings() {
    }
}
