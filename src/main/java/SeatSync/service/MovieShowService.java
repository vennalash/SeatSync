
package SeatSync.service;

import java.util.List;

import org.springframework.stereotype.Service;

import SeatSync.entity.Movie;
import SeatSync.entity.MovieShow;
import SeatSync.repository.MovieRepository;
import SeatSync.repository.MovieShowRepository;

@Service
public class MovieShowService {

    private final MovieShowRepository movieShowRepository;
    private final MovieRepository movieRepository;

    public MovieShowService(
            MovieShowRepository movieShowRepository,
            MovieRepository movieRepository) {
        this.movieShowRepository = movieShowRepository;
        this.movieRepository = movieRepository;
    }

    public MovieShow createShow(MovieShow movieShow, Long movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new RuntimeException("Movie not found"));

        if (movieShow.getCinemaName() == null
                || movieShow.getCinemaName().isBlank()) {
            throw new RuntimeException("Cinema name is required");
        }

        if (movieShow.getCity() == null
                || movieShow.getCity().isBlank()) {
            throw new RuntimeException("City is required");
        }

        if (movieShow.getShowDate() == null
                || movieShow.getShowTime() == null) {
            throw new RuntimeException("Show date and time are required");
        }

        if (movieShow.getTicketPrice() == null
                || movieShow.getTicketPrice() <= 0) {
            throw new RuntimeException("Enter a valid ticket price");
        }

        movieShow.setMovie(movie);

        return movieShowRepository.save(movieShow);
    }

    public List<MovieShow> getShowsByMovie(Long movieId) {

        if (!movieRepository.existsById(movieId)) {
            throw new RuntimeException("Movie not found");
        }

        return movieShowRepository.findByMovieId(movieId);
    }

    public List<MovieShow> getAllShows() {
        return movieShowRepository.findAll();
    }
}
