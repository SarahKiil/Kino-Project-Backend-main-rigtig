package kinoproject.project.film.api_facade;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import kinoproject.project.film.dto.FilmOmdbResponse;

@Service
public class OmdbFacade {
    RestTemplate restTemplate;

    public OmdbFacade() {
        restTemplate = new RestTemplate();
    }

    @Value("${app.omdb-key}")
    String API_KEY ;

    String OMDB_URL = "http://www.omdbapi.com";

    public FilmOmdbResponse getFilm(String imdbId) {
        String url = OMDB_URL+"/?apikey=" + API_KEY + "&plot=full" + "&i=";
        FilmOmdbResponse filmDTO = restTemplate.getForObject(url+imdbId, FilmOmdbResponse.class);
        return filmDTO;
    }
}
