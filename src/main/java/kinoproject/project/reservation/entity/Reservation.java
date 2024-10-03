package kinoproject.project.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import kinoproject.project.seat.entity.Seat;
import kinoproject.project.showing.entity.Showing;
import kinoproject.project.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany
    private List<Seat> seats = new ArrayList<>();
    private Integer totalPrice;
    @ManyToOne
    private User customer;
    @ManyToOne
    private Showing showing;
}
