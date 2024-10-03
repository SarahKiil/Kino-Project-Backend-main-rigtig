package kinoproject.project.theater.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kinoproject.project.theater.enity.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Integer> {
}
