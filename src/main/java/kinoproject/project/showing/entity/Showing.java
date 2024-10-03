package kinoproject.project.showing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import kinoproject.project.film.entity.Film;
import kinoproject.project.seat.entity.Seat;
import kinoproject.project.theater.enity.Theater;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
public class Showing{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Film film;
    @OneToMany
    private List<Seat> seats = new ArrayList<>();
    @ManyToOne
    private Theater theater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timeAndDate;
    private Double ticketPrice;
}
