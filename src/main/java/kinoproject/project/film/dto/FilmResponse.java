package kinoproject.project.film.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import kinoproject.project.film.entity.Film;
import kinoproject.project.showing.entity.Showing;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmResponse {
    private Integer id;
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String poster;
    private String metascore;
    private String imdbRating;
    private boolean isOngoing;
    private List<Showing> showings = new ArrayList<>();

    public FilmResponse(Film film) {
        this.showings = film.getShowings();
        this.id = film.getId();
        this.title = film.getTitle();
        this.year = film.getYear();
        this.rated = film.getRated();
        this.released = film.getReleased();
        this.runtime = film.getRuntime();
        this.genre = film.getGenre();
        this.director = film.getDirector();
        this.writer = film.getWriter();
        this.actors = film.getActors();
        this.plot = film.getPlot();
        this.poster = film.getPoster();
        this.metascore = film.getMetascore();
        this.imdbRating = film.getImdbRating();
        this.isOngoing = film.isOngoing();
    }
}
