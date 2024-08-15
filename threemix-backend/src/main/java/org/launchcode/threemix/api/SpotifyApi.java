package org.launchcode.threemix.api;

import jakarta.servlet.http.HttpSession;
import org.launchcode.threemix.json.SpotifyUser;
import org.launchcode.threemix.json.TokenResponse;
import org.launchcode.threemix.secret.ClientConstants;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpotifyApi {

    private final RestTemplate restTemplate;
    private String accessToken;
    private String refreshToken;

    public static SpotifyApi fromSession(HttpSession session, String accessToken, RestTemplate restTemplate) {
        return Optional.ofNullable((SpotifyApi) session.getAttribute("spotifyApi"))
                .orElseGet(() -> {
                    SpotifyApi api = new SpotifyApi(restTemplate, accessToken, null);
                    session.setAttribute("spotifyApi", api);
                    return api;
                });
    }

    public SpotifyApi(RestTemplate restTemplate, String accessToken, String refreshToken) {
        this.restTemplate = restTemplate;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    private <T> T get(String url, Class<T> responseType) {
        return httpCall(HttpMethod.GET, url, null, responseType);
    }

    private <T> T post(String url, Object body, Class<T> responseType) {
        return httpCall(HttpMethod.POST, url, body, responseType);
    }

    private <T> T httpCall(HttpMethod method, String url, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.exchange(url, method, entity, responseType)
                    .getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                System.out.println("Refresh!");
                refresh();

                headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + accessToken);
                entity = new HttpEntity<>(body, headers);
                return restTemplate.exchange(url, method, entity, responseType)
                        .getBody();
            }
            throw e;
        }
    }

    public TokenResponse refresh() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((ClientConstants.CLIENT_ID + ":" + ClientConstants.CLIENT_SECRET).getBytes()));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        TokenResponse tokenResponse = restTemplate.postForObject("https://accounts.spotify.com/api/token", request,
                TokenResponse.class);
        Optional.ofNullable(tokenResponse).ifPresent(t -> {
            accessToken = t.access_token();
            refreshToken = Optional.ofNullable(t.refresh_token()).orElse(refreshToken);
        });
        return tokenResponse;
    }

    public SpotifyUser me() {
        return get("https://api.spotify.com/v1/me", SpotifyUser.class);
    }

    public Map<?, ?> recommendations(List<String> chosenGenres) {
        String genres = String.join(",", chosenGenres);
        return get("https://api.spotify.com/v1/recommendations?seed_genres=" + genres,
                Map.class);
    }

    public String createPlaylist(String spotifyId, String playlistName, String description) {
        Map<String, String> requestBody =
                Map.of("name", playlistName, "description", description, "public", "true");
        return post("https://api.spotify.com/v1/users/" + spotifyId + "/playlists", requestBody, Map.class)
                .get("id").toString();
    }

    public void addTracksToPlaylist(String playlistId, List<String> trackUris) {
        Map<String, List<String>> requestBody = Map.of("uris", trackUris);
        post("https://api.spotify.com/v1/playlists/" + playlistId + "/tracks", requestBody, Map.class);
    }
}
