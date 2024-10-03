package kinoproject.project.seat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kinoproject.project.seat.entity.Seat;
@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

}
