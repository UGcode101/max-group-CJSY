package org.launchcode.threemix.controller;

import org.launchcode.threemix.json.TokenResponse;
import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.launchcode.threemix.service.UserService;
import org.launchcode.threemix.service.SessionStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class PlaylistExportController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionStorage<TokenResponse> tokenStorage;

    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @PostMapping(value = "/generateTrackList", produces = "application/json")
    public Map<String, Object> generateTrackList(@CookieValue("accessToken") String accessToken,
                                                 @RequestParam List<String> chosenGenres,
                                                 HttpSession session) {
        // Retrieve the Spotify ID from the session
        String spotifyId = (String) session.getAttribute("spotifyId");
        User user = userService.findUserBySpotifyId(spotifyId);

        // Fetch blocked artists and songs from the database
        List<String> blockedArtists = userService.findBlockedArtistByUser(user)
                .stream().map(BlockedArtist::getArtistId).toList();

        List<String> blockedSongs = userService.findBlockedSongsByUser(user)
                .stream().map(BlockedSong::getSongId).toList();

        // Generate recommendations from Spotify API
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> trackRecommendations = getRecommendations(chosenGenres, entity);

        // Filter out blocked artists and songs
        filterRecommendations(trackRecommendations, blockedArtists, blockedSongs);

        return trackRecommendations;
    }

    // Method to fetch recommendations from Spotify API
    private Map<String, Object> getRecommendations(List<String> chosenGenres, HttpEntity<String> entity) {
        // Build the Spotify API request URL with chosen genres
        String url = buildSpotifyRecommendationUrl(chosenGenres);

        // Fetch recommendations from Spotify API
        return restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
    }

    // Method to build the Spotify recommendation URL
    private String buildSpotifyRecommendationUrl(List<String> chosenGenres) {
        String genres = String.join(",", chosenGenres);
        return "https://api.spotify.com/v1/recommendations?seed_genres=" + genres;
    }

    // Method to filter out blocked artists and songs
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
                .toList(); // Using .toList() for simplicity

        recommendations.put("tracks", filteredTracks);
    }
}