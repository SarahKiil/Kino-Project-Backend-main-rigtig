package kinoproject.project.film.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import kinoproject.project.film.api_facade.OmdbFacade;
import kinoproject.project.film.dto.FilmOmdbResponse;
import kinoproject.project.film.dto.FilmResponse;
import kinoproject.project.film.entity.Film;
import kinoproject.project.film.repository.FilmRepository;

import java.util.List;

@Service
public class FilmService {

    FilmRepository filmRepository;

//    @Autowired
//    AzureTranslate translator;

    @Autowired
    OmdbFacade omdbFacade;

    public FilmService(FilmRepository filmRepository, OmdbFacade omdbFacade) {
        this.omdbFacade = omdbFacade;
        this.filmRepository = filmRepository;
    }
    public FilmResponse getFilmById(int id){
        Film film = filmRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Film with this ID does not exist"));
        return new FilmResponse(film);
    }
    public List<FilmResponse> getAllFilms(){
        List<Film> films = filmRepository.findAll();
        List<FilmResponse> filmResponses = films.stream().map(film -> new FilmResponse(film)).toList();
        return filmResponses;

    }

    public Film getFilmByImdbId(String imdbId) {
        return filmRepository.findByImdbID(imdbId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }


    public Film addFilm(String imdbId) throws JsonProcessingException {
        FilmOmdbResponse dto = omdbFacade.getFilm(imdbId);
        //String dkPlot = translator.translate(dto.getPlot());

        Film film = Film.builder()
                .title(dto.getTitle())
                .year(dto.getYear())
                .rated(dto.getRated())
                .released(dto.getReleased())
                .runtime(dto.getRuntime())
                .genre(dto.getGenre())
                .director(dto.getDirector())
                .writer(dto.getWriter())
                .actors(dto.getActors())
                .metascore(dto.getMetascore())
                .imdbRating(dto.getImdbRating())
                .imdbVotes(dto.getImdbVotes())
                .website(dto.getWebsite())
                .response(dto.getResponse())
                .plot(dto.getPlot())
                //.plotDK(dkPlot)
                .poster(dto.getPoster())
                .imdbID(dto.getImdbID())
                .build();
        try {
            filmRepository.save(film);
            return film;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add movie");
        }
    }

    public FilmOmdbResponse inspectFilm(String imdbId) throws JsonProcessingException {
        return omdbFacade.getFilm(imdbId);
    }
}
