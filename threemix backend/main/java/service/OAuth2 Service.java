import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuth2Service {
    @Autowired
    private SpotifyConfig spotifyConfig;

    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";

    public String getAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        String clientId = spotifyConfig.getClientId();
        String clientSecret = spotifyConfig.getClientSecret();

        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("code", authorizationCode);
        body.put("redirect_uri", spotifyConfig.getRedirectUri());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);

        Map<String, Object> response = restTemplate.postForObject(TOKEN_URL, body, Map.class, headers);

        return response != null ? (String) response.get("access_token") : null;
    }
}