package kinoproject.project.showing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import kinoproject.project.film.entity.Film;
import kinoproject.project.seat.entity.Seat;
import kinoproject.project.showing.entity.Showing;
import kinoproject.project.theater.enity.Theater;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowingResponse {
    private int id;
    private Film film;
    private List<Seat> seats = new ArrayList<>();
    private Theater theater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timeAndDate;
    private Double ticketPrice;
    private String movieTitle;

    public ShowingResponse(Showing showing, boolean includeSeats) {
        this.movieTitle = showing.getFilm().getTitle();
        this.id = showing.getId();
        this.theater = showing.getTheater();
        this.timeAndDate = showing.getTimeAndDate();
        this.ticketPrice = showing.getTicketPrice();
        if (includeSeats){
        this.seats = showing.getSeats();}
    }
}
