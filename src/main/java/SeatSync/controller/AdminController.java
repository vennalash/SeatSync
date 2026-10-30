
package SeatSync.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SeatSync.repository.UserRepository;
import SeatSync.repository.BookingRepository;
import SeatSync.repository.MovieRepository;
import SeatSync.repository.EventRepository;
import SeatSync.repository.HotelRepository;
import SeatSync.repository.SportsFacilityRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final EventRepository eventRepository;
    private final HotelRepository hotelRepository;
    private final SportsFacilityRepository sportsFacilityRepository;

    public AdminController(
            UserRepository userRepository,
            BookingRepository bookingRepository,
            MovieRepository movieRepository,
            EventRepository eventRepository,
            HotelRepository hotelRepository,
            SportsFacilityRepository sportsFacilityRepository) {

        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.eventRepository = eventRepository;
        this.hotelRepository = hotelRepository;
        this.sportsFacilityRepository = sportsFacilityRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {

        Map<String, Long> stats = Map.of(
            "totalUsers", userRepository.count(),
            "totalBookings", bookingRepository.count(),
            "totalMovies", movieRepository.count(),
            "totalEvents", eventRepository.count(),
            "totalHotels", hotelRepository.count(),
            "totalSportsFacilities", sportsFacilityRepository.count()
        );

        return ResponseEntity.ok(stats);
    }
}
