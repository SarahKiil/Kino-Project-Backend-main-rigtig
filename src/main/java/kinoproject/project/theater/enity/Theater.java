package kinoproject.project.theater.enity;

import jakarta.persistence.*;
import lombok.*;
import kinoproject.project.showing.entity.Showing;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer seatCount;
    private int rowLength;

    @OneToMany
    private List<Showing> showings;

}
