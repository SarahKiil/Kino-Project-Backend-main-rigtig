package kinoproject.project.seat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import kinoproject.project.seat.entity.Seat;
import kinoproject.project.showing.entity.Showing;

@Getter
@Setter
@NoArgsConstructor
public class SeatRequest {
    private Showing showing;
    private Integer seatNumber;
    public SeatRequest(Seat seat) {
        this.seatNumber = seat.getSeatNumber();
    }
}
