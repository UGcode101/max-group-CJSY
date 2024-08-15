package org.launchcode.threemix.controller;

import jakarta.servlet.http.HttpSession;
import org.launchcode.threemix.api.SpotifyApi;
import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.launchcode.threemix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PlaylistExportController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @PostMapping(value = "/generateTrackList", produces = "application/json")
    public Map<String, Object> generateTrackList(@CookieValue("accessToken") String accessToken,
                                                 @RequestParam List<String> chosenGenres,
                                                 HttpSession session) {
        String spotifyId = userService.getUserId(accessToken, session);
        User user = userService.findUserBySpotifyId(spotifyId);
        SpotifyApi api = SpotifyApi.fromSession(session, accessToken, restTemplate);
        Map<String, Object> trackRecommendations = (Map<String, Object>) api.recommendations(chosenGenres);

        List<String> blockedArtists = userService.findBlockedArtistByUser(user)
                .stream().map(BlockedArtist::getArtistId).toList();

        List<String> blockedSongs = userService.findBlockedSongsByUser(user)
                .stream().map(BlockedSong::getSongId).toList();

        filterRecommendations(trackRecommendations, blockedArtists, blockedSongs);

        String playlistId = createPlaylistOnSpotify(api, spotifyId, "Generated Playlist");

        addTracksToSpotifyPlaylist(api, playlistId, trackRecommendations);

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

    private String createPlaylistOnSpotify(SpotifyApi api, String spotifyId, String playlistName) {
        try {
            return api.createPlaylist(spotifyId, playlistName);
        } catch (Exception e) {
            System.err.println("Error creating playlist on Spotify: " + e.getMessage());
            return null;
        }
    }

    private void addTracksToSpotifyPlaylist(SpotifyApi api, String playlistId, Map<String, Object> trackRecommendations) {
        List<Map<String, Object>> tracks = (List<Map<String, Object>>) trackRecommendations.get("tracks");
        List<String> trackUris = tracks.stream()
                .map(track -> "spotify:track:" + track.get("id"))
                .toList();
        try {
            api.addTracksToPlaylist(playlistId, trackUris);
        } catch (Exception e) {
            System.err.println("Error adding tracks to playlist: " + e.getMessage());
        }
    }
}