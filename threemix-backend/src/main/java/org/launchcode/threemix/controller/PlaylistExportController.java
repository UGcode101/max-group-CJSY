package org.launchcode.threemix.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class PlaylistExportController {
    private final RestTemplate restTemplate;

    public PlaylistExportController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getPlaylistDetails", produces = "application/json")
    public Map<String, Object> getPlaylistDetails(@CookieValue("accessToken") String accessToken,
                                                  @RequestParam String playlistId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> playlistDetails = restTemplate.getForObject(
                "https://api.spotify.com/v1/playlists/" + playlistId, Map.class, entity);

        return playlistDetails;
    }
}