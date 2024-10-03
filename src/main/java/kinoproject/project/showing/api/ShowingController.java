package kinoproject.project.showing.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import kinoproject.project.showing.dto.AddShowingRequest;
import kinoproject.project.showing.dto.ShowingResponse;
import kinoproject.project.showing.service.ShowingService;

import java.util.List;

@RestController
@RequestMapping("api/showings")
public class ShowingController {

    ShowingService showingService;

    public ShowingController(ShowingService showingService) {
        this.showingService = showingService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShowingResponse addShowing(@RequestBody AddShowingRequest body){
        return showingService.addShowing(body);
    }

    @GetMapping()
    public List<ShowingResponse> showingResponseList(){
        return showingService.getAllShowings(false);
    }

    @GetMapping("/includeSeats")
    public List<ShowingResponse> showingResponseListIncludeSeats(){
        return showingService.getAllShowings(true);
    }

    @GetMapping("/{id}")
    public ShowingResponse showingResponse(@PathVariable int id){
        return showingService.getShowingById(id,false);
    }

    @GetMapping("/{id}/includeSeats")
    public ShowingResponse showingResponseIncludeSeats(@PathVariable int id){
        return showingService.getShowingById(id,true);
    }

    @GetMapping("findAllByFilmId/{filmId}")
    public List<ShowingResponse> showingResponseListByFilmId(@PathVariable int filmId){
        return showingService.getShowingsByFilm(filmId);
    }



}
