package org.launchcode.threemix.model;

import jakarta.persistence.*;

@Entity
public class BlockedSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spotify_id", referencedColumnName = "spotifyId")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blocked_song_stats_id", referencedColumnName = "id")
    private BlockedSongStats blockedSongStats;

    private String songId;

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

    public BlockedSongStats getBlockedSongStats() {
        return blockedSongStats;
    }

    public void setBlockedSongStats(BlockedSongStats blockedSongStats) {
        this.blockedSongStats = blockedSongStats;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}