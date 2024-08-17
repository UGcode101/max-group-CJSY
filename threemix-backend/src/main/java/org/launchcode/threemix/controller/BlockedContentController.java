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
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BlockedContentController {

    @Autowired
    private UserService userService;

    // POST and GET for blocked artists
    @PostMapping("/blockedArtist")
    public void blockArtist(@RequestParam String artistId,
                            @CookieValue("accessToken") String accessToken,
                            HttpSession session) {
        String spotifyId = userService.getUserId(accessToken, session);
        User user = userService.findUserBySpotifyId(spotifyId);
        if (user.getBlockedArtists().stream().map(BlockedArtist::getArtistId).noneMatch(id -> id.equals(artistId))) {
            BlockedArtist blockedArtist = new BlockedArtist();
            blockedArtist.setArtistId(artistId);
            blockedArtist.setUser(user);
            user.getBlockedArtists().add(blockedArtist);
            userService.saveUser(user);
        }
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
    public void blockSong(@RequestParam String songId,
                          @CookieValue("accessToken") String accessToken,
                          HttpSession session) {
        String spotifyId = userService.getUserId(accessToken, session);
        User user = userService.findUserBySpotifyId(spotifyId);
        if (user.getBlockedSongs().stream().map(BlockedSong::getSongId).noneMatch(id -> id.equals(songId))) {
            BlockedSong blockedSong = new BlockedSong();
            blockedSong.setSongId(songId);
            blockedSong.setUser(user);
            user.getBlockedSongs().add(blockedSong);
            userService.saveUser(user);
        }
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