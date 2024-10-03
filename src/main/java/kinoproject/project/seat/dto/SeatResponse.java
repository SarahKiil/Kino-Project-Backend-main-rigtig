package kinoproject.project.seat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import kinoproject.project.reservation.entity.Reservation;
import kinoproject.project.seat.entity.Seat;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatResponse {

    private int id;
    private Integer seatNumber;
    private Reservation reservation;
    private int showingIdOnShowing;
    private Boolean isReserved;

    public SeatResponse(Seat seat) {
        this.id = seat.getId();
        this.seatNumber = seat.getSeatNumber();
        this.showingIdOnShowing = seat.getShowingIdOnShowing();
        this.isReserved = seat.getIsReserved();
    }
}
