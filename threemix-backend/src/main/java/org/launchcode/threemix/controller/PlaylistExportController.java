package org.launchcode.threemix.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.launchcode.threemix.json.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PlaylistExportController {
    private final RestTemplate restTemplate;

    public PlaylistExportController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getSpotifyId", produces = "application/json")
    public String getSpotifyId(@CookieValue("accessToken") String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        Profile profile = restTemplate.getForObject("https://api.spotify.com/v1/me", Profile.class, entity);
        return profile.getId();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/createPlaylist", produces = "application/json")
    public String createPlaylist(@CookieValue("accessToken") String accessToken,
                                 @RequestParam String userId,
                                 @RequestParam String playlistName,
                                 @RequestParam String playlistDescription) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", playlistName);
        requestBody.put("description", playlistDescription);
        requestBody.put("public", false);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map<String, Object> response = restTemplate.postForObject(
                "https://api.spotify.com/v1/users/" + userId + "/playlists", request, Map.class);

        return response.get("id").toString();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/addTracksToPlaylist", produces = "application/json")
    public void addTracksToPlaylist(@CookieValue("accessToken") String accessToken,
                                    @RequestParam String playlistId,
                                    @RequestParam List<String> trackUris) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("uris", trackUris);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        restTemplate.postForObject("https://api.spotify.com/v1/playlists/" + playlistId + "/tracks", request, String.class);
    }
}