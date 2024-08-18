package org.launchcode.threemix.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spotify_id", referencedColumnName = "spotifyId")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_stats_id", referencedColumnName = "id")
    private GenreStats genreStats;

    private String playlistName;
    private LocalDateTime exportedAt;

    // Default constructor
    public UserHistory() {}

    // Constructor for general user action logging
    public UserHistory(User user, String playlistName, LocalDateTime exportedAt) {
        this.user = user;
        this.playlistName = playlistName;
        this.exportedAt = exportedAt;
    }

    // Constructor for genre stats logging
    public UserHistory(User user, GenreStats genreStats) {
        this.user = user;
        this.genreStats = genreStats;
        this.exportedAt = LocalDateTime.now(); // Automatically set the exported time to now
    }

    // Constructor for logging blocked artist or song actions
    public UserHistory(User user, String entityType, String entityId, LocalDateTime exportedAt) {
        this.user = user;
        this.playlistName = entityType + ": " + entityId; // Store entity type and ID in playlistName for simplicity
        this.exportedAt = exportedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GenreStats getGenreStats() {
        return genreStats;
    }

    public void setGenreStats(GenreStats genreStats) {
        this.genreStats = genreStats;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public LocalDateTime getExportedAt() {
        return exportedAt;
    }

    public void setExportedAt(LocalDateTime exportedAt) {
        this.exportedAt = exportedAt;
    }
}