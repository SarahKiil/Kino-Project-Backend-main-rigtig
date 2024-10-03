package kinoproject.project.film.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import kinoproject.project.film.api_facade.OmdbFacade;
import kinoproject.project.film.dto.FilmOmdbResponse;
import kinoproject.project.film.dto.FilmResponse;
import kinoproject.project.film.entity.Film;
import kinoproject.project.film.repository.FilmRepository;
import kinoproject.security.TestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FilmServiceTest {

    @Autowired
    private FilmService filmService;

    @MockBean
    private FilmRepository filmRepository;

    @MockBean
    private OmdbFacade omdbFacade;

    @BeforeAll
    public static void iniSetUp() {
        System.setProperty(TestUtils.h2UrlName, TestUtils.h2UrlValue);
        System.setProperty(TestUtils.h2UsernameName, TestUtils.h2UsernameValue);
        System.setProperty(TestUtils.h2PassName, TestUtils.h2PassValue);
        System.setProperty(TestUtils.tokenSecretName, TestUtils.tokenSecretValue);
    }

    @AfterAll
    public static void tearDown() {
        System.clearProperty(TestUtils.h2UrlName);
        System.clearProperty(TestUtils.h2UsernameName);
        System.clearProperty(TestUtils.h2PassName);
        System.clearProperty(TestUtils.tokenSecretName);
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetAllFilms() throws JsonProcessingException {
        FilmOmdbResponse filmOmdbResponse = makeFilmOmdbResponse();
        when(omdbFacade.getFilm("tt7286456")).thenReturn(filmOmdbResponse);
        Film film = makeFilm();
        when(filmRepository.save(film)).thenReturn(film);
        when(filmRepository.findAll()).thenReturn(List.of(film));

        filmService.addFilm("tt7286456");

        List<FilmResponse> films = filmService.getAllFilms();
        assertEquals(1, films.size());
    }

    @Test
    void testGetFilmById() throws JsonProcessingException {
        FilmOmdbResponse filmOmdbResponse = makeFilmOmdbResponse();
        when(omdbFacade.getFilm("tt7286456")).thenReturn(filmOmdbResponse);
        Film film = makeFilm();
        when(filmRepository.save(film)).thenReturn(film);
        when(filmRepository.findById(1)).thenReturn(java.util.Optional.of(film));

        filmService.addFilm("tt7286456");

        FilmResponse filmResponse = filmService.getFilmById(1);
        assertEquals("Joker", filmResponse.getTitle());
    }

    @Test
    void testGetFilmByImdbId() throws JsonProcessingException {
        FilmOmdbResponse filmOmdbResponse = makeFilmOmdbResponse();
        when(omdbFacade.getFilm("tt7286456")).thenReturn(filmOmdbResponse);
        Film film = makeFilm();
        when(filmRepository.save(film)).thenReturn(film);
        when(filmRepository.findByImdbID("tt7286456")).thenReturn(java.util.Optional.of(film));

        filmService.addFilm("tt7286456");

        Film filmResponse = filmService.getFilmByImdbId("tt7286456");
        assertEquals("Joker", filmResponse.getTitle());
    }

    @Test
    void testAddFilm() throws JsonProcessingException {
        FilmOmdbResponse filmOmdbResponse = makeFilmOmdbResponse();
        when(omdbFacade.getFilm("tt7286456")).thenReturn(filmOmdbResponse);
        Film film = makeFilm();
        when(filmRepository.save(film)).thenReturn(film);

        Film filmResponse = filmService.addFilm("tt7286456");
        assertEquals("Joker", filmResponse.getTitle());
    }

    public FilmOmdbResponse makeFilmOmdbResponse() {
        FilmOmdbResponse filmOmdbResponse = new FilmOmdbResponse();
        filmOmdbResponse.setImdbID("tt7286456");
        filmOmdbResponse.setTitle("Joker");
        filmOmdbResponse.setYear("2019");
        filmOmdbResponse.setRated("R");
        filmOmdbResponse.setReleased("04 Oct 2019");
        filmOmdbResponse.setRuntime("122 min");
        filmOmdbResponse.setGenre("Crime, Drama, Thriller");
        filmOmdbResponse.setDirector("Todd Phillips");
        filmOmdbResponse.setWriter("Todd Phillips, Scott Silver");
        filmOmdbResponse.setActors("Joaquin Phoenix, Robert De Niro, Zazie Beetz, Frances Conroy");
        filmOmdbResponse.setPlot("In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.");
        filmOmdbResponse.setLanguage("English");
        filmOmdbResponse.setCountry("USA, Canada");
        filmOmdbResponse.setAwards("Won 2 Oscars. Another 112 wins & 234 nominations.");
        filmOmdbResponse.setPoster("https://m.media-amazon.com/images/M/MV5BMWMwMGU5MmUtMzQyZC00M2M0LWE5MjAtMzY2MzY2MzY2MzY2XkEyXkFqcGdeQXVyMTA4NjE0NjEy._V1_SX300.jpg");
        filmOmdbResponse.setRatings(null);
        filmOmdbResponse.setMetascore("59");
        filmOmdbResponse.setImdbRating("8.5");
        filmOmdbResponse.setImdbVotes("1,069,017");
        filmOmdbResponse.setImdbID("tt7286456");
        filmOmdbResponse.setType("movie");
        filmOmdbResponse.setDvd("17 Dec 2019");
        filmOmdbResponse.setBoxOffice("$335,451,311");
        filmOmdbResponse.setProduction("Bron Studios, Creative Wealth Media Finance, DC Comics");
        filmOmdbResponse.setWebsite("N/A");
        filmOmdbResponse.setResponse("True");
        return filmOmdbResponse;
    }

    public Film makeFilm() {
        Film film = new Film();
        film.setImdbID("tt7286456");
        film.setTitle("Joker");
        film.setYear("2019");
        film.setRated("R");
        film.setReleased("04 Oct 2019");
        film.setRuntime("122 min");
        film.setGenre("Crime, Drama, Thriller");
        film.setDirector("Todd Phillips");
        film.setWriter("Todd Phillips, Scott Silver");
        film.setActors("Joaquin Phoenix, Robert De Niro, Zazie Beetz, Frances Conroy");
        film.setPlot("In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.");
        film.setPoster("https://m.media-amazon.com/images/M/MV5BMWMwMGU5MmUtMzQyZC00M2M0LWE5MjAtMzY2MzY2MzY2MzY2XkEyXkFqcGdeQXVyMTA4NjE0NjEy._V1_SX300.jpg");
        film.setMetascore("59");
        film.setImdbRating("8.5");
        film.setImdbVotes("1,069,017");
        film.setImdbID("tt7286456");
        film.setWebsite("N/A");
        film.setResponse("True");
        return film;
    }
}