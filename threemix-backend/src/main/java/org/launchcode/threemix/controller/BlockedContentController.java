package org.launchcode.threemix.controller;

import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.launchcode.threemix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BlockedContentController {

    @Autowired
    private UserService userService;

    // POST and GET for blocked artists
    @PostMapping("/blockedArtist")
    public BlockedArtist blockArtist(@RequestParam String artistId, HttpSession session) {
        String spotifyId = (String) session.getAttribute("spotifyId");
        User user = userService.findUserBySpotifyId(spotifyId);
        BlockedArtist blockedArtist = new BlockedArtist();
        blockedArtist.setArtistId(artistId);
        blockedArtist.setUser(user);
        return userService.saveBlockedArtist(blockedArtist);
    }

    @GetMapping("/blockedArtist")
    public List<BlockedArtist> getBlockedArtists(HttpSession session) {
        String spotifyId = (String) session.getAttribute("spotifyId");
        User user = userService.findUserBySpotifyId(spotifyId);
        return userService.findBlockedArtistByUser(user);
    }

    // Deleting a specific blocked artist
    @DeleteMapping("/blockedArtist/{id}")
    public void unblockArtist(@PathVariable Long id) {
        userService.deleteBlockedArtistById(id);
    }

    // POST and GET for blocked songs
    @PostMapping("/blockedSong")
    public BlockedSong blockSong(@RequestParam String songId, HttpSession session) {
        String spotifyId = (String) session.getAttribute("spotifyId");
        User user = userService.findUserBySpotifyId(spotifyId);
        BlockedSong blockedSong = new BlockedSong();
        blockedSong.setSongId(songId);
        blockedSong.setUser(user);
        return userService.saveBlockedSong(blockedSong);
    }

    @GetMapping("/blockedSong")
    public List<BlockedSong> getBlockedSongs(HttpSession session) {
        String spotifyId = (String) session.getAttribute("spotifyId");
        User user = userService.findUserBySpotifyId(spotifyId);
        return userService.findBlockedSongsByUser(user);
    }

    // Deleting a specific blocked song
    @DeleteMapping("/blockedSong/{id}")
    public void unblockSong(@PathVariable Long id) {
        userService.deleteBlockedSongById(id);
    }
}