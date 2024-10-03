package kinoproject.project.film.entity;



import jakarta.persistence.*;
import lombok.*;
import kinoproject.project.showing.entity.Showing;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;

    @Column(length = 2000)
    private String plot;
    @Column(length = 2000)
    private String plotDK;
    private String poster;

    private String metascore;
    private String imdbRating;
    private String imdbVotes;

    @Column(unique = true)
    private String imdbID;
    private String website;
    private String response;
    private boolean isOngoing;

    @OneToMany
    private List<Showing> showings;

    public Film(int id){
        this.id = id;
    }

}
