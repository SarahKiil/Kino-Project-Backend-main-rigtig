package kinoproject.project.film.api;




import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import kinoproject.project.film.dto.FilmOmdbResponse;
import kinoproject.project.film.dto.FilmResponse;
import kinoproject.project.film.entity.Film;
import kinoproject.project.film.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("api/films")
public class FilmController {

    FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping
    public List<FilmResponse> getAllFilms(){
        return filmService.getAllFilms();
    }
    @GetMapping("/{id}")
    public FilmResponse getFilmById(@PathVariable int id){
        return filmService.getFilmById(id);
    }


    @RequestMapping("/imdbid/{imdbId}")
    public Film getFilm(@PathVariable String imdbId) {
        return filmService.getFilmByImdbId(imdbId);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/{imdbId}")
    public Film addfilm(@PathVariable String imdbId) throws JsonProcessingException {
        return filmService.addFilm(imdbId);
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/inspectFilm/{imdbId}")
    public FilmOmdbResponse inspectFilm(@PathVariable String imdbId) throws JsonProcessingException {
        return filmService.inspectFilm(imdbId);
    }


}
