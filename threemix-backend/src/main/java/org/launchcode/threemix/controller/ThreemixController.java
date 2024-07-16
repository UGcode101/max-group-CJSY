package org.launchcode.threemix.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.launchcode.threemix.json.Profile;
import org.launchcode.threemix.json.TokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
public class ThreemixController {
    private final String state = "slfpcerbta";
    private final String scope = "user-read-private user-read-email playlist-modify-private playlist-modify-public";
    private final String client_id = "your_client_id"; // Add your client ID here
    private final String client_secret = "your_client_secret"; // Add your client secret here
    private final String redirect_uri = "http://localhost:8080/callback";
    private final RestTemplate restTemplate;

    public ThreemixController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/hello", produces = "application/json")
    public Profile getHello() {
        return new Profile("userJenn");
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/login")
    public RedirectView login(RedirectAttributes attributes) {
        attributes.addAttribute("response_type", "code");
        attributes.addAttribute("client_id", client_id);
        attributes.addAttribute("scope", scope);
        attributes.addAttribute("redirect_uri", redirect_uri);
        attributes.addAttribute("state", state);

        return new RedirectView("https://accounts.spotify.com/authorize");
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/callback", produces = "application/json")
    public void callback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        if (!this.state.equals(state)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid state parameter");
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("redirect_uri", redirect_uri);
        map.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            TokenResponse tokenResponse = restTemplate.postForObject("https://accounts.spotify.com/api/token", request,
                    TokenResponse.class);
            Optional.ofNullable(tokenResponse).ifPresentOrElse(
                    t -> {
                        Cookie accessTokenCookie = new Cookie("accessToken", t.access_token());
                        accessTokenCookie.setHttpOnly(true);
                        accessTokenCookie.setSecure(true); // Ensure this is set to true in production
                        response.addCookie(accessTokenCookie);
                    },
                    () -> {
                        try {
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve access token");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
            response.sendRedirect("http://localhost:5173");
        } catch (HttpClientErrorException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + e.getMessage());
        } catch (RestClientException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during token retrieval: " + e.getMessage());
        }
    }
}