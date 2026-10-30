
package SeatSync.service;

import java.util.List;

import org.springframework.stereotype.Service;

import SeatSync.entity.Movie;
import SeatSync.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Get only active movies for customers
    public List<Movie> getActiveMovies() {
        return movieRepository.findAll()
                .stream()
                .filter(Movie::isActive)
                .toList();
    }

    // Get all movies for admin
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Add movie
    public Movie addMovie(Movie movie) {
        validateMovie(movie);
        movie.setId(null);
        movie.setActive(true);
        return movieRepository.save(movie);
    }

    // Update movie
    public Movie updateMovie(Long id, Movie updatedMovie) {

        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Movie not found"));

        validateMovie(updatedMovie);

        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setLanguage(updatedMovie.getLanguage());
        existingMovie.setDurationMinutes(
                updatedMovie.getDurationMinutes());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setPosterUrl(updatedMovie.getPosterUrl());
        existingMovie.setTicketPrice(updatedMovie.getTicketPrice());
        existingMovie.setCinemaName(updatedMovie.getCinemaName());
        existingMovie.setCity(updatedMovie.getCity());
        existingMovie.setTheaterAddress(
                updatedMovie.getTheaterAddress());
        existingMovie.setShowDate(updatedMovie.getShowDate());
        existingMovie.setShowTime(updatedMovie.getShowTime());

        return movieRepository.save(existingMovie);
    }

    // Deactivate movie instead of permanently deleting it
    public Movie deleteMovie(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Movie not found"));

        movie.setActive(false);

        return movieRepository.save(movie);
    }

    // Activate movie again
    public Movie activateMovie(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Movie not found"));

        movie.setActive(true);

        return movieRepository.save(movie);
    }

    // Validate movie details
    private void validateMovie(Movie movie) {

        if (movie.getTitle() == null ||
                movie.getTitle().isBlank()) {
            throw new RuntimeException("Movie title is required");
        }

        if (movie.getTicketPrice() == null ||
                movie.getTicketPrice() <= 0) {
            throw new RuntimeException("Enter a valid ticket price");
        }

        if (movie.getDurationMinutes() == null ||
                movie.getDurationMinutes() <= 0) {
            throw new RuntimeException("Enter a valid movie duration");
        }
    }
    
}
