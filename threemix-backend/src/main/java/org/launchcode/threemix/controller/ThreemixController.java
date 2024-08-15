package org.launchcode.threemix.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.launchcode.threemix.api.SpotifyApi;
import org.launchcode.threemix.json.TokenResponse;
import org.launchcode.threemix.secret.ClientConstants;
import org.launchcode.threemix.service.StateService;
import org.launchcode.threemix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ThreemixController {

    private static final Logger logger = Logger.getLogger(ThreemixController.class.getName());
    private static final String SCOPE = "user-read-private user-read-email playlist-modify-private playlist-modify-public";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StateService stateService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public RedirectView login(RedirectAttributes attributes, HttpSession session) {
        // Generate a random state using StateController
        String state = stateService.generateState(session.getId());

        attributes.addAttribute("response_type", "code");
        attributes.addAttribute("client_id", ClientConstants.CLIENT_ID);
        attributes.addAttribute("scope", SCOPE);
        attributes.addAttribute("redirect_uri", REDIRECT_URI);
        // Use the generated state
        attributes.addAttribute("state", state);

        return new RedirectView("https://accounts.spotify.com/authorize");
    }

    @GetMapping(value = "/callback")
    public void callback(@RequestParam String code, @RequestParam String state, HttpServletResponse response, HttpSession session) throws IOException {
        if (!stateService.validateState(session.getId(), state)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid state parameter");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((ClientConstants.CLIENT_ID + ":" + ClientConstants.CLIENT_SECRET).getBytes()));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            TokenResponse tokenResponse = restTemplate.postForObject("https://accounts.spotify.com/api/token", request,
                    TokenResponse.class);
            Optional.ofNullable(tokenResponse).ifPresentOrElse(
                    t -> {
                        session.setAttribute("spotifyApi", new SpotifyApi(restTemplate, t.access_token(), t.refresh_token()));
                        Cookie accessTokenCookie = new Cookie("accessToken", t.access_token());
                        accessTokenCookie.setSecure(true); // Ensure this is set to true in production
                        response.addCookie(accessTokenCookie);
                        userService.createIfNewUser(t.access_token(), session);
                    },
                    () -> {
                        try {
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve access token");
                        } catch (IOException e) {
                            logger.severe("Error sending error response: " + e.getMessage());
                        }
                    }
            );
            response.sendRedirect("http://localhost:5173");
        } catch (HttpClientErrorException e) {
            logger.severe("Unauthorized: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + e.getMessage());
        } catch (RestClientException e) {
            logger.severe("Error during token retrieval: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during token retrieval: " + e.getMessage());
        }
    }

    @PostMapping(value = "/refresh")
    public void refresh(HttpServletResponse response, HttpSession session) throws IOException {
        SpotifyApi api = SpotifyApi.fromSession(session, null, restTemplate);
        TokenResponse tokenResponse = api.refresh();
        Cookie accessTokenCookie = new Cookie("accessToken", tokenResponse.access_token());
        accessTokenCookie.setSecure(true);
        response.addCookie(accessTokenCookie);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletResponse response, HttpSession session) throws IOException {
        session.invalidate();
        Cookie accessTokenCookie = new Cookie("accessToken", "");
        accessTokenCookie.setSecure(true);
        response.addCookie(accessTokenCookie);
    }
}