package org.launchcode.threemix.service;

import jakarta.servlet.http.HttpSession;
import org.launchcode.threemix.json.SpotifyUser;
import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.launchcode.threemix.repository.BlockedArtistRepository;
import org.launchcode.threemix.repository.BlockedSongRepository;
import org.launchcode.threemix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockedArtistRepository blockedArtistRepository;

    @Autowired
    private BlockedSongRepository blockedSongRepository;

    @Autowired
    private RestTemplate restTemplate;

    // User related methods
    public void createIfNewUser(String accessToken, HttpSession session) {
        String userId = getUserId(accessToken, session);
        Optional.ofNullable(userRepository.findBySpotifyId(userId))
                .orElseGet(() -> userRepository.save(new User(userId)));
    }

    public User findUserBySpotifyId(String spotifyId) {
        return userRepository.findBySpotifyId(spotifyId);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // BlockedArtist related methods
    public List<BlockedArtist> findBlockedArtistByUser(User user) {
        return blockedArtistRepository.findBlockedArtistByUser(user);
    }

    public BlockedArtist saveBlockedArtist(BlockedArtist blockedArtist) {
        return blockedArtistRepository.save(blockedArtist);
    }

    public void deleteBlockedArtistById(Long id) {
        blockedArtistRepository.deleteById(id);
    }

    // BlockedSong related methods
    public List<BlockedSong> findBlockedSongsByUser(User user) {
        return blockedSongRepository.findBlockedSongsByUser(user);
    }

    public BlockedSong saveBlockedSong(BlockedSong blockedSong) {
        return blockedSongRepository.save(blockedSong);
    }

    public void deleteBlockedSongById(Long id) {
        blockedSongRepository.deleteById(id);
    }

    public String getUserId(String accessToken, HttpSession session) {
        return ((SpotifyUser) Optional.ofNullable(session.getAttribute("userInfo"))
                .orElseGet(() -> fetchUserInfo(accessToken, session)))
                .id();
    }

    private SpotifyUser fetchUserInfo(String accessToken, HttpSession session) {
        System.out.println("Fetching user info");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        SpotifyUser user = restTemplate.exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                entity,
                SpotifyUser.class).getBody();
        if (user == null) {
            throw new RuntimeException("Unable to fetch user id");
        }
        session.setAttribute("userInfo", user);
        return user;
    }

}