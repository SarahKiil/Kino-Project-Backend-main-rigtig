package kinoproject.project.film.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import kinoproject.project.film.entity.Film;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    Optional<Film> findByImdbID(String imdbID);
    Optional<Film> findById(int id);
}
