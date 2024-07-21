package org.launchcode.threemix.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.launchcode.threemix.json.Profile;
import org.launchcode.threemix.json.TokenResponse;
import org.launchcode.threemix.secret.ClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
public class ThreemixController {
    //TODO: Generate random state and store with user session. Delete from user session after single use.
    private final String state = "slfpcerbta";
    //TODO: make sure we need all the requested scopes
    private final String scope = "user-read-private user-read-email";
    private final String client_id = ClientConstants.CLIENT_ID;
    private final String client_secret = ClientConstants.CLIENT_SECRET;
    private final String redirect_uri = "http://localhost:8080/callback";
    @Autowired
    private RestTemplate restTemplate;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/login")
    public RedirectView login(RedirectAttributes attributes) {

        attributes.addAttribute("response_type", "code");
        attributes.addAttribute("client_id", client_id);
        attributes.addAttribute("scope", scope);
        attributes.addAttribute("redirect_uri", redirect_uri);
        attributes.addAttribute("state", state);

        return new RedirectView("https://accounts.spotify.com/authorize");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/callback")
    public void callback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        if(!this.state.equals(state)) {
            throw new IllegalStateException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("redirect_uri", redirect_uri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body , headers);

        TokenResponse tokenResponse = restTemplate.postForObject("https://accounts.spotify.com/api/token", request,
                TokenResponse.class);
        Optional.ofNullable(tokenResponse).ifPresent(t ->
            response.addCookie(new Cookie("accessToken", t.access_token())));
        response.sendRedirect("http://localhost:5173");
    }
}
