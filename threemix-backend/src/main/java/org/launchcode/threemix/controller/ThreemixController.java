package org.launchcode.threemix.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.launchcode.threemix.json.Profile;
import org.launchcode.threemix.json.TokenRequest;
import org.launchcode.threemix.json.TokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@RestController
public class ThreemixController {
    private final String state = "slfpcerbta";
    private final String scope = "user-read-private user-read-email";
    private final String client_id = "0dae3d34f1ec4c369587cdac57da5c5a";
    private final String client_secret = "";
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
    public Profile callback(@RequestParam String code, @RequestParam String state) {
        if(!this.state.equals(state)) {
            throw new IllegalStateException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()));

        TokenRequest tokenRequest = new TokenRequest("authorization_code", code, redirect_uri);

//        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("code", code,
//                "redirect_uri", redirect_uri,
//                "grantType", "authorization_code"), headers);

        TokenResponse tokenResponse = restTemplate.postForObject("https://accounts.spotify.com/api/token", tokenRequest,
                TokenResponse.class);

        return new Profile(tokenResponse.access_token());
    }
}
