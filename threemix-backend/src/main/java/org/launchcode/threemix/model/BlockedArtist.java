package org.launchcode.threemix.model;

import jakarta.persistence.*;

@Entity
public class BlockedArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spotify_id", referencedColumnName = "spotifyId")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blocked_artist_stats_id", referencedColumnName = "id")
    private BlockedArtistStats blockedArtistStats;

    private String artistId;  // This field was missing

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

    public BlockedArtistStats getBlockedArtistStats() {
        return blockedArtistStats;
    }

    public void setBlockedArtistStats(BlockedArtistStats blockedArtistStats) {
        this.blockedArtistStats = blockedArtistStats;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}