package org.launchcode.threemix.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String spotifyId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockedArtist> blockedArtists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockedSong> blockedSongs;

    public User() {}

    public User(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public List<BlockedArtist> getBlockedArtists() {
        return blockedArtists;
    }

    public void setBlockedArtists(List<BlockedArtist> blockedArtists) {
        this.blockedArtists = blockedArtists;
    }

    public List<BlockedSong> getBlockedSongs() {
        return blockedSongs;
    }

    public void setBlockedSongs(List<BlockedSong> blockedSongs) {
        this.blockedSongs = blockedSongs;
    }
}