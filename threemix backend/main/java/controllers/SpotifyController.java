import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SpotifyController {
    @Autowired
    private SpotifyService spotifyService;

    @GetMapping("/fetch-genres")
    public String fetchGenres(@RequestHeader("Authorization") String accessToken) {
        spotifyService.fetchAndSaveGenres(accessToken);
        return "Genres fetched and saved successfully!";
    }

    @PostMapping("/fetch-song")
    public String fetchSong(@RequestHeader("Authorization") String accessToken, @RequestParam String songId) {
        spotifyService.fetchAndSaveSong(accessToken, songId);
        return "Song fetched and saved successfully!";
    }

    @PostMapping("/fetch-artist")
    public String fetchArtist(@RequestHeader("Authorization") String accessToken, @RequestParam String artistId) {
        spotifyService.fetchAndSaveArtist(accessToken, artistId);
        return "Artist fetched and saved successfully!";
    }

    // CRUD endpoints for songs, artists, genres, and profiles

    @GetMapping("/songs")
    public List<Song> getAllSongs() {
        return spotifyService.getAllSongs();
    }

    @GetMapping("/songs/{id}")
    public Optional<Song> getSongById(@PathVariable Long id) {
        return spotifyService.getSongById(id);
    }

    @PostMapping("/songs")
    public Song saveSong(@RequestBody Song song) {
        return spotifyService.saveSong(song);
    }

    @DeleteMapping("/songs/{id}")
    public void deleteSong(@PathVariable Long id) {
        spotifyService.deleteSong(id);
    }

    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        return spotifyService.getAllArtists();
    }

    @GetMapping("/artists/{id}")
    public Optional<Artist> getArtistById(@PathVariable Long id) {
        return spotifyService.getArtistById(id);
    }

    @PostMapping("/artists")
    public Artist saveArtist(@RequestBody Artist artist) {
        return spotifyService.saveArtist(artist);
    }

    @DeleteMapping("/artists/{id}")
    public void deleteArtist(@PathVariable Long id) {
        spotifyService.deleteArtist(id);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return spotifyService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getGenreById(@PathVariable Long id) {
        return spotifyService.getGenreById(id);
    }

    @PostMapping("/genres")
    public Genre saveGenre(@RequestBody Genre genre) {
        return spotifyService.saveGenre(genre);
    }

    @DeleteMapping("/genres/{id}")
    public void deleteGenre(@PathVariable Long id) {
        spotifyService.deleteGenre(id);
    }

    @GetMapping("/profiles")
    public List<Profile> getAllProfiles() {
        return spotifyService.getAllProfiles();
    }

    @GetMapping("/profiles/{id}")
    public Optional<Profile> getProfileById(@PathVariable Long id) {
        return spotifyService.getProfileById(id);
    }

    @PostMapping("/profiles")
    public Profile saveProfile(@RequestBody Profile profile) {
        return spotifyService.saveProfile(profile);
    }

    @DeleteMapping("/profiles/{id}")
    public void deleteProfile(@PathVariable Long id) {
