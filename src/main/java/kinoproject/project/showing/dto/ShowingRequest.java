package kinoproject.project.showing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import kinoproject.project.film.entity.Film;
import kinoproject.project.seat.entity.Seat;
import kinoproject.project.showing.entity.Showing;
import kinoproject.project.theater.enity.Theater;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowingRequest {
    private int id;
    private Film film;
    private List<Seat> seats;
    private Theater theater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timeAndDate;
    private Double ticketPrice;

    public static Showing getShowingEntity(ShowingRequest showingRequest){
        Showing showing = Showing.builder().ticketPrice(showingRequest.getTicketPrice())
                .timeAndDate(showingRequest.getTimeAndDate())
                .theater(showingRequest.getTheater())
                .film(showingRequest.getFilm())
                .seats(showingRequest.getSeats()).build();
        return showing;
    }

    public ShowingRequest(Showing showing) {
        this.film = showing.getFilm();
        this.seats = showing.getSeats();
        this.theater = showing.getTheater();
        this.timeAndDate = showing.getTimeAndDate();
        this.ticketPrice = showing.getTicketPrice();
    }
}
