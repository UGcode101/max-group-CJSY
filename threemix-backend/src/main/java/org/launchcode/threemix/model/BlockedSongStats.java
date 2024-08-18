package org.launchcode.threemix.model;

import jakarta.persistence.*;

@Entity
public class BlockedSongStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String songId;

    // Constructor with songId parameter
    public BlockedSongStats(String songId) {
        this.songId = songId;
    }

    // Default constructor
    public BlockedSongStats() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}