import javax.persistence.*;
import java.util.List;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;

    @ManyToMany
    private List<Song> likedSongs;

    @ManyToMany
    private List<Song> excludedSongs;

    @ManyToMany
    private List<Artist> likedArtists;

    @ManyToMany
    private List<Artist> excludedArtists;

    @ManyToMany
    private List<Genre> preferredGenres;

    // Getters and Setters
}