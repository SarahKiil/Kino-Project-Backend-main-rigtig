package kinoproject.project.showing.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import kinoproject.project.film.entity.Film;
import kinoproject.project.film.repository.FilmRepository;
import kinoproject.project.seat.entity.Seat;
import kinoproject.project.seat.repository.SeatRepository;
import kinoproject.project.showing.dto.AddShowingRequest;
import kinoproject.project.showing.dto.ShowingResponse;
import kinoproject.project.showing.entity.Showing;
import kinoproject.project.showing.repository.ShowingRepository;
import kinoproject.project.theater.enity.Theater;
import kinoproject.project.theater.repository.TheaterRepository;

import java.util.List;

@Service
public class ShowingService {

    FilmRepository filmRepository;
    TheaterRepository theaterRepository;

    SeatRepository seatRepository;

    ShowingRepository showingRepository;

    public ShowingService(FilmRepository filmRepository, TheaterRepository theaterRepository, ShowingRepository showingRepository, SeatRepository seatRepository) {
        this.filmRepository = filmRepository;
        this.theaterRepository = theaterRepository;
        this.showingRepository = showingRepository;
        this.seatRepository = seatRepository;
    }
    public ShowingResponse addShowing(AddShowingRequest addShowingRequest) {
        Showing showing = new Showing();
        showing = showingRepository.save(showing);
        int showingId = showing.getId();

        int filmId = addShowingRequest.getFilmId();
        int theaterId = addShowingRequest.getTheaterId();

        Film film = filmRepository.findById(filmId).orElseThrow();
        showing.setFilm(film);

        Theater theater = theaterRepository.findById(theaterId).orElseThrow();
        showing.setTheater(theater);

        showing.setTicketPrice(addShowingRequest.getTicketPrice());
        showing.setTimeAndDate(addShowingRequest.getTimeAndDate());
        for (int i = 0; i < theater.getSeatCount(); i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(i + 1);

            seat.setShowingIdOnShowing(showingId);

            seat = seatRepository.save(seat);
            showing.getSeats().add(seat);
        }

        showingRepository.save(showing);
        return new ShowingResponse(showing,true);
//        LocalDateTime ldt = addShowingRequest.getTimeAndDate();
    }
    public List<ShowingResponse> getAllShowings(boolean includeSeats){
        List<Showing> showings = showingRepository.findAll();
        List<ShowingResponse> showingResponses = showings.stream().map(showing -> new ShowingResponse(showing,includeSeats)).toList();
        return showingResponses;

    }
    public ShowingResponse getShowingById(int id, boolean includeSeats){
        Showing showing = showingRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Showing with this ID does not exist"));
        return new ShowingResponse(showing,includeSeats);
    }

    public List<ShowingResponse> getShowingsByFilm(int filmId){
        Film film = filmRepository.findById(filmId).orElseThrow();
        List<Showing> showings = showingRepository.findShowingsByFilm(film);
        List<ShowingResponse> showingResponses = showings.stream().map(showing -> new ShowingResponse(showing,false)).toList();
        return showingResponses;
    }

}

