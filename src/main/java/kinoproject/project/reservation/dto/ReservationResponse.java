package kinoproject.project.reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import kinoproject.project.reservation.entity.Reservation;
import kinoproject.project.seat.entity.Seat;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {
    private List<Seat> seats = new ArrayList<>();
    private Double totalPrice;
    private String customerEmail;
    private String customerUsername;
    private int theaterId;
    private String showingDate;
    private String title;
    private int reservationId;
    private int showingId;

    public ReservationResponse(Reservation reservation) {
        this.seats              = reservation.getSeats();
        this.totalPrice         = reservation.getShowing().getTicketPrice() * reservation.getSeats().size();
        this.customerEmail      = reservation.getCustomer().getEmail();
        this.customerUsername   = reservation.getCustomer().getUsername();
        this.theaterId          = reservation.getShowing().getTheater().getId();
        this.showingDate        = reservation.getShowing().getTimeAndDate().toString();
        this.title              = reservation.getShowing().getFilm().getTitle();
        this.reservationId      = reservation.getId();
        this.showingId          = reservation.getShowing().getId();
    }
}
