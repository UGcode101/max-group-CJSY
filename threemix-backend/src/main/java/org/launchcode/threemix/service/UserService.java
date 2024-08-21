package org.launchcode.threemix.service;

import jakarta.servlet.http.HttpSession;
import org.launchcode.threemix.api.SpotifyApi;
import org.launchcode.threemix.json.SpotifyUser;
import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.launchcode.threemix.model.UserHistory;
import org.launchcode.threemix.model.GenreStats;
import org.launchcode.threemix.model.BlockedArtistStats;
import org.launchcode.threemix.model.BlockedSongStats;
import org.launchcode.threemix.repository.BlockedArtistRepository;
import org.launchcode.threemix.repository.BlockedSongRepository;
import org.launchcode.threemix.repository.UserHistoryRepository;
import org.launchcode.threemix.repository.UserRepository;
import org.launchcode.threemix.repository.GenreStatsRepository;
import org.launchcode.threemix.repository.BlockedArtistStatsRepository;
import org.launchcode.threemix.repository.BlockedSongStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
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
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private GenreStatsRepository genreStatsRepository;

    @Autowired
    private BlockedArtistStatsRepository blockedArtistStatsRepository;

    @Autowired
    private BlockedSongStatsRepository blockedSongStatsRepository;

    @Autowired
    private RestTemplate restTemplate;

    // User related methods
    public void createIfNewUser(HttpSession session) {
        String userId = getUserId(session);
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

    public String getUserId(HttpSession session) {
        return ((SpotifyUser) Optional.ofNullable(session.getAttribute("userInfo"))
                .orElseGet(() -> fetchUserInfo(session)))
                .id();
    }

    private SpotifyUser fetchUserInfo(HttpSession session) {
        SpotifyApi api = SpotifyApi.fromSession(session);
        SpotifyUser user = api.me();
        if (user == null) {
            throw new RuntimeException("Unable to fetch user id");
        }
        session.setAttribute("userInfo", user);
        return user;
    }

    // UserHistory and Statistics related methods
    public void logUserAction(User user, String playlistName) {
        UserHistory history = new UserHistory(user, playlistName, LocalDateTime.now());
        userHistoryRepository.save(history);
    }

    public void logGenreUsage(User user, List<String> genres) {
        GenreStats genreStats = new GenreStats(genres);
        genreStatsRepository.save(genreStats);

        UserHistory history = new UserHistory(user, genreStats);
        userHistoryRepository.save(history);
    }

    public void logBlockedArtist(User user, String artistId) {
        BlockedArtistStats blockedArtistStats = new BlockedArtistStats(artistId);
        blockedArtistStatsRepository.save(blockedArtistStats);

        BlockedArtist blockedArtist = new BlockedArtist();
        blockedArtist.setUser(user);
        blockedArtist.setBlockedArtistStats(blockedArtistStats);
        blockedArtistRepository.save(blockedArtist);

        UserHistory history = new UserHistory(user, "Blocked Artist", artistId, LocalDateTime.now());
        userHistoryRepository.save(history);
    }

    public void logBlockedSong(User user, String songId) {
        BlockedSongStats blockedSongStats = new BlockedSongStats(songId);
        blockedSongStatsRepository.save(blockedSongStats);

        BlockedSong blockedSong = new BlockedSong();
        blockedSong.setUser(user);
        blockedSong.setBlockedSongStats(blockedSongStats);
        blockedSongRepository.save(blockedSong);

        UserHistory history = new UserHistory(user, "Blocked Song", songId, LocalDateTime.now());
        userHistoryRepository.save(history);
    }

    public List<UserHistory> getUserHistory(User user) {
        return userHistoryRepository.findByUserId(user.getId());
    }
}