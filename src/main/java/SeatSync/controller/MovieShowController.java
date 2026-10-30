
package SeatSync.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import SeatSync.entity.MovieShow;
import SeatSync.service.MovieShowService;

@RestController
@RequestMapping("/api/movie-shows")
public class MovieShowController {

    private final MovieShowService movieShowService;

    public MovieShowController(MovieShowService movieShowService) {
        this.movieShowService = movieShowService;
    }

    // Create a show for a movie
    @PostMapping("/movie/{movieId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieShow createShow(
            @PathVariable Long movieId,
            @RequestBody MovieShow movieShow) {

        return movieShowService.createShow(movieShow, movieId);
    }

    // Get all shows for one movie
    @GetMapping("/movie/{movieId}")
    public List<MovieShow> getShowsByMovie(
            @PathVariable Long movieId) {

        return movieShowService.getShowsByMovie(movieId);
    }

    // Get all cinema shows
    @GetMapping
    public List<MovieShow> getAllShows() {
        return movieShowService.getAllShows();
    }
}
