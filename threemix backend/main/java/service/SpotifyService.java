import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SpotifyService {
    private static final String SPOTIFY_GENRES_API_URL = "https://api.spotify.com/v1/recommendations/available-genre-seeds";

    @Autowired
    private GenreRepository genreRepository;

    public void fetchAndSaveGenres(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String[]> response = restTemplate.exchange(
                SPOTIFY_GENRES_API_URL, HttpMethod.GET, entity, String[].class);

        String[] genres = response.getBody();

        if (genres != null) {
            Arrays.stream(genres).forEach(genreName -> {
                Genre genre = new Genre();
                genre.setName(genreName);
                genreRepository.save(genre);
            });
        }import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

        @Service
        public class SpotifyService {
            private static final String SPOTIFY_GENRES_API_URL = "https://api.spotify.com/v1/recommendations/available-genre-seeds";
            private static final String SPOTIFY_TRACKS_API_URL = "https://api.spotify.com/v1/tracks/";
            private static final String SPOTIFY_ARTISTS_API_URL = "https://api.spotify.com/v1/artists/";

            @Autowired
            private GenreRepository genreRepository;

            @Autowired
            private SongRepository songRepository;

            @Autowired
            private ArtistRepository artistRepository;

            @Autowired
            private ProfileRepository profileRepository;

            public void fetchAndSaveGenres(String accessToken) {
                RestTemplate restTemplate = new RestTemplate();
                String[] genres = restTemplate.getForObject(SPOTIFY_GENRES_API_URL, String[].class);

                if (genres != null) {
                    Arrays.stream(genres).forEach(genreName -> {
                        Genre genre = new Genre();
                        genre.setName(genreName);
                        genreRepository.save(genre);
                    });
                }
            }

            public void fetchAndSaveSong(String accessToken, String songId) {
                RestTemplate restTemplate = new RestTemplate();
                Song song = restTemplate.getForObject(SPOTIFY_TRACKS_API_URL + songId, Song.class);

                if (song != null) {
                    songRepository.save(song);
                }
            }

            public void fetchAndSaveArtist(String accessToken, String artistId) {
                RestTemplate restTemplate = new RestTemplate();
                Artist artist = restTemplate.getForObject(SPOTIFY_ARTISTS_API_URL + artistId, Artist.class);

                if (artist != null) {
                    artistRepository.save(artist);
                }
            }

            // CRUD methods for songs, artists, genres, and profiles

            public List<Song> getAllSongs() {
                return songRepository.findAll();
            }

            public Optional<Song> getSongById(Long id) {
                return songRepository.findById(id);
            }

            public Song saveSong(Song song) {
                return songRepository.save(song);
            }

            public void deleteSong(Long id) {
                songRepository.deleteById(id);
            }

            public List<Artist> getAllArtists() {
                return artistRepository.findAll();
            }

            public Optional<Artist> getArtistById(Long id) {
                return artistRepository.findById(id);
            }

            public Artist saveArtist(Artist artist) {
                return artistRepository.save(artist);
            }

            public void deleteArtist(Long id) {
                artistRepository.deleteById(id);
            }

            public List<Genre> getAllGenres() {
                return genreRepository.findAll();
            }

            public Optional<Genre> getGenreById(Long id) {
                return genreRepository.findById(id);
            }

            public Genre saveGenre(Genre genre) {
                return genreRepository.save(genre);
            }

            public void deleteGenre(Long id) {
                genreRepository.deleteById(id);
            }

            public List<Profile> getAllProfiles() {
                return profileRepository.findAll();
            }

            public Optional<Profile> getProfileById(Long id) {
                return profileRepository.findById(id);
            }

            public Profile saveProfile(Profile profile) {
                return profileRepository.save(profile);
            }

            public void deleteProfile(Long id) {
                profileRepository.deleteById(id);
            }
        }
    }
}