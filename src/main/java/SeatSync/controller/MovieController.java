
package SeatSync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import SeatSync.entity.Movie;
import SeatSync.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Customer: Get active movies
    @GetMapping
    public List<Movie> getActiveMovies() {
        return movieService.getActiveMovies();
    }

    // Admin: Get all movies
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Admin: Add movie
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        try {
            return ResponseEntity.ok(movieService.addMovie(movie));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Admin: Update movie
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(
            @PathVariable Long id,
            @RequestBody Movie movie) {
        try {
            return ResponseEntity.ok(
                movieService.updateMovie(id, movie)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Admin: Deactivate movie
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                movieService.deleteMovie(id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Admin: Activate movie
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateMovie(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                movieService.activateMovie(id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Admin: Search movies
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/search")
    public ResponseEntity<List<Movie>> searchMovies(
            @RequestParam String keyword) {

        List<Movie> results = movieService.getAllMovies()
                .stream()
                .filter(movie ->
                    movie.getTitle() != null &&
                    movie.getTitle().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .toList();

        return ResponseEntity.ok(results);
    }
}
