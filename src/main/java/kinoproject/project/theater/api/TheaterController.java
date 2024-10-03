package kinoproject.project.theater.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import kinoproject.project.theater.dto.TheaterRequest;
import kinoproject.project.theater.dto.TheaterResponse;
import kinoproject.project.theater.service.TheaterService;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    TheaterService theaterService;

    public TheaterController(TheaterService theaterService){
        this.theaterService= theaterService;
    }

    @GetMapping
    public List<TheaterResponse> getAllTheaters(){
        return theaterService.getAllTheaters();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TheaterResponse addTheater(@RequestBody TheaterRequest theaterRequest){
        return theaterService.addTheater(theaterRequest);
    }


}
