
package SeatSync.repository;

import SeatSync.entity.Booking;
import SeatSync.entity.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    boolean existsByBookingReference(String bookingReference);

    // Find active bookings for a particular cinema show
    List<Booking> findByShowIdAndServiceTypeAndStatusIn(
            Long showId,
            String serviceType,
            List<BookingStatus> statuses
    );

    // Get seats already reserved for a show
    @Query("""
        SELECT b.seats
        FROM Booking b
        WHERE b.showId = :showId
          AND b.serviceType = 'MOVIE'
          AND b.status IN :statuses
          AND b.seats IS NOT NULL
    """)
    List<String> findBookedSeatsByShow(
            @Param("showId") Long showId,
            @Param("statuses") List<BookingStatus> statuses
    );

    // Get bookings for a customer
    List<Booking> findByCustomerNameIgnoreCase(String customerName);

    // Find active sports bookings for a particular facility and date
    List<Booking> findByServiceTypeAndServiceIdAndSportsDateAndStatusIn(
            String serviceType,
            Long serviceId,
            LocalDate sportsDate,
            List<BookingStatus> statuses
    );
}
