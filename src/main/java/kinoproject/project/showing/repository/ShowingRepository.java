package kinoproject.project.showing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kinoproject.project.film.entity.Film;
import kinoproject.project.showing.entity.Showing;

import java.util.List;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Integer> {

    List<Showing> findShowingsByFilm(Film film);

}
