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

        List<String> blockedArtists = userService.findBlockedArtistByUser(user)
                .stream().map(BlockedArtist::getArtistId).toList();

        List<String> blockedSongs = userService.findBlockedSongsByUser(user)
                .stream().map(BlockedSong::getSongId).toList();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> trackRecommendations = getRecommendations(chosenGenres, entity);

        filterRecommendations(trackRecommendations, blockedArtists, blockedSongs);

        String playlistId = createPlaylistOnSpotify(spotifyId, "Generated Playlist", entity);

        addTracksToSpotifyPlaylist(playlistId, trackRecommendations, entity);

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
                .toList();

        recommendations.put("tracks", filteredTracks);
    }

    private String createPlaylistOnSpotify(String spotifyId, String playlistName, HttpEntity<String> entity) {
        String url = "https://api.spotify.com/v1/users/" + spotifyId + "/playlists";
        Map<String, String> requestBody = Map.of("name", playlistName, "description", "Generated playlist", "public", "false");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, entity.getHeaders());

        try {
            Map<String, Object> response = restTemplate.postForObject(url, requestEntity, Map.class);
            return (String) response.get("id"); // Return the playlist ID
        } catch (Exception e) {
            System.err.println("Error creating playlist on Spotify: " + e.getMessage());
            return null;
        }
    }

    // Method to add tracks to the newly created Spotify playlist
    private void addTracksToSpotifyPlaylist(String playlistId, Map<String, Object> trackRecommendations, HttpEntity<String> entity) {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

        List<Map<String, Object>> tracks = (List<Map<String, Object>>) trackRecommendations.get("tracks");
        List<String> trackUris = tracks.stream()
                .map(track -> "spotify:track:" + track.get("id"))
                .toList();

        Map<String, List<String>> requestBody = Map.of("uris", trackUris);
        HttpEntity<Map<String, List<String>>> requestEntity = new HttpEntity<>(requestBody, entity.getHeaders());

        try {
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        } catch (Exception e) {
            System.err.println("Error adding tracks to playlist: " + e.getMessage());
        }
    }
}