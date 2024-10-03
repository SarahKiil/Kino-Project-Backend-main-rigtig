package kinoproject.project.theater.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import kinoproject.project.showing.entity.Showing;
import kinoproject.project.theater.enity.Theater;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TheaterRequest {
    private Integer seatCount;

    private List<Showing> showings;

    private Integer rowLength;

    public static Theater getTheaterEntity(TheaterRequest theaterRequest) {
        return new Theater().builder()
                .seatCount(theaterRequest.getSeatCount())
                .rowLength(theaterRequest.getRowLength())
                .build();
    }

    public TheaterRequest(Theater theater) {
        this.seatCount = theater.getSeatCount();
        this.rowLength = theater.getRowLength();
    }
}
