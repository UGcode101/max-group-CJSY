package org.launchcode.threemix.controller;

import jakarta.servlet.http.HttpSession;
import org.launchcode.threemix.api.SpotifyApi;
import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.launchcode.threemix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PlaylistExportController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/generateTrackList", produces = "application/json")
    public Map<String, Object> generateTrackList(@RequestParam List<String> chosenGenres,
                                                 HttpSession session) {
        String spotifyId = userService.getUserId(session);
        User user = userService.findUserBySpotifyId(spotifyId);
        SpotifyApi api = SpotifyApi.fromSession(session);
        Map<String, Object> trackRecommendations = (Map<String, Object>) api.recommendations(chosenGenres);

        List<String> blockedArtists = userService.findBlockedArtistByUser(user)
                .stream().map(BlockedArtist::getArtistId).toList();

        List<String> blockedSongs = userService.findBlockedSongsByUser(user)
                .stream().map(BlockedSong::getSongId).toList();

        filterRecommendations(trackRecommendations, blockedArtists, blockedSongs);

        // Log genre usage in UserHistory and GenreStats
        userService.logGenreUsage(user, chosenGenres);

        return trackRecommendations;
    }

    private void filterRecommendations(Map<String, Object> recommendations, List<String> blockedArtists, List<String> blockedSongs) {
        List<Map<String, Object>> tracks = (List<Map<String, Object>>) recommendations.get("tracks");

        List<Map<String, Object>> filteredTracks = tracks.stream()
                .filter(track -> {
                    Map<String, Object> trackDetails = (Map<String, Object>) track;
                    List<Map<String, Object>> artists = (List<Map<String, Object>>) trackDetails.get("artists");
                    String trackId = (String) trackDetails.get("id");

                    boolean isBlockedArtist = artists.stream()
                            .map(artist -> (String) artist.get("id"))
                            .anyMatch(blockedArtists::contains);

                    boolean isBlockedSong = blockedSongs.contains(trackId);

                    return !isBlockedArtist && !isBlockedSong;
                })
                .toList();

        recommendations.put("tracks", filteredTracks);
    }

    @PostMapping(value = "/exportPlaylist")
    public void exportPlaylist(@RequestParam String name,
                               @RequestParam String description,
                               @RequestParam List<String> trackIds,
                               HttpSession session) {
        System.out.println("Exporting playlist: " + name);
        String spotifyId = userService.getUserId(session);
        User user = userService.findUserBySpotifyId(spotifyId);
        SpotifyApi api = SpotifyApi.fromSession(session);

        List<String> trackUris = trackIds.stream()
                .map(id -> "spotify:track:" + id)
                .toList();
        String playlistId = api.createPlaylist(spotifyId, name, description);
        api.addTracksToPlaylist(playlistId, trackUris);

        // Log only the playlist name in UserHistory
        userService.logUserAction(user, name);
    }
}