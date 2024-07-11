import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {
    @Autowired
    private OAuth2Service oAuth2Service;

    @Autowired
    private SpotifyService spotifyService;

    @GetMapping("/authorize")
    public String authorize() {
        String clientId = spotifyConfig.getClientId();
        String redirectUri = spotifyConfig.getRedirectUri();
        String scopes = "user-read-private user-read-email";

        String url = "https://accounts.spotify.com/authorize" +
                "?client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scopes;

        return "redirect:" + url;
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code) {
        String accessToken = oAuth2Service.getAccessToken(code);

        if (accessToken != null) {
            // Use access token to fetch data
            spotifyService.fetchAndSaveGenres(accessToken);
            return "Genres fetched and saved successfully!";
        } else {
            return "Failed to get access token";
        }
    }
}