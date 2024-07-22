package org.launchcode.threemix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PlaylistExportController {

    @Autowired
    private RestTemplate restTemplate;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/generateTrackList", produces = "application/json")
    public Map<String, Object> generateTrackList(@CookieValue("accessToken") String accessToken,
                                                 @RequestParam List<String> chosenGenres) {
        // Placeholder: Simulate fetching blocked artists and songs
        Map<String, List<String>> blockedList = getBlockedList();

        // Generate recommendations from Spotify API
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> trackRecommendations = getRecommendations(chosenGenres, blockedList, entity);

        return trackRecommendations;
    }

    // Placeholder method to simulate fetching blocked artists and songs
    private Map<String, List<String>> getBlockedList() {
        Map<String, List<String>> blockedList = new HashMap<>();
        blockedList.put("artists", List.of("blockedArtistId1", "blockedArtistId2"));
        blockedList.put("songs", List.of("blockedSongId1", "blockedSongId2"));
        return blockedList;
    }

    // Placeholder method to simulate fetching recommendations from Spotify API
    private Map<String, Object> getRecommendations(List<String> chosenGenres, Map<String, List<String>> blockedList, HttpEntity<String> entity) {
        // Build the Spotify API request URL with chosen genres and blocked items
        String url = buildSpotifyRecommendationUrl(chosenGenres, blockedList);

        // Fetch recommendations from Spotify API
        return restTemplate.getForObject(url, Map.class, entity);
    }

    // Placeholder method to build the Spotify recommendation URL
    private String buildSpotifyRecommendationUrl(List<String> chosenGenres, Map<String, List<String>> blockedList) {
        String genres = String.join(",", chosenGenres);
        StringBuilder url = new StringBuilder("https://api.spotify.com/v1/recommendations?seed_genres=" + genres);

        if (!blockedList.get("artists").isEmpty()) {
            url.append("&seed_artists=").append(String.join(",", blockedList.get("artists")));
        }
        if (!blockedList.get("songs").isEmpty()) {
            url.append("&seed_tracks=").append(String.join(",", blockedList.get("songs")));
        }

        return url.toString();
    }
}