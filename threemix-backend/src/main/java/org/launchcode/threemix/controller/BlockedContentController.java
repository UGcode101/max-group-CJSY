package org.launchcode.threemix.controller;

import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.launchcode.threemix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlockedContentController {

    @Autowired
    private UserService userService;

    @PostMapping("/blockArtist")
    public BlockedArtist blockArtist(@RequestParam String spotifyId, @RequestParam String artistId) {
        User user = userService.findUserBySpotifyId(spotifyId);
        BlockedArtist blockedArtist = new BlockedArtist();
        blockedArtist.setArtistId(artistId);
        blockedArtist.setUser(user);
        return userService.saveBlockedArtist(blockedArtist);
    }

    @PostMapping("/blockSong")
    public BlockedSong blockSong(@RequestParam String spotifyId, @RequestParam String songId) {
        User user = userService.findUserBySpotifyId(spotifyId);
        BlockedSong blockedSong = new BlockedSong();
        blockedSong.setSongId(songId);
        blockedSong.setUser(user);
        return userService.saveBlockedSong(blockedSong);
    }

    @GetMapping("/blockedArtists")
    public List<BlockedArtist> getBlockedArtists(@RequestParam String spotifyId) {
        User user = userService.findUserBySpotifyId(spotifyId);
        return userService.findBlockedArtistsByUser(user);
    }

    @GetMapping("/blockedSongs")
    public List<BlockedSong> getBlockedSongs(@RequestParam String spotifyId) {
        User user = userService.findUserBySpotifyId(spotifyId);
        return userService.findBlockedSongsByUser(user);
    }

    @DeleteMapping("/unblockArtist")
    public void unblockArtist(@RequestParam Long id) {
        BlockedArtist blockedArtist = new BlockedArtist();
        blockedArtist.setId(id);
        userService.deleteBlockedArtist(blockedArtist);
    }

    @DeleteMapping("/unblockSong")
    public void unblockSong(@RequestParam Long id) {
        BlockedSong blockedSong = new BlockedSong();
        blockedSong.setId(id);
        userService.deleteBlockedSong(blockedSong);
    }
}