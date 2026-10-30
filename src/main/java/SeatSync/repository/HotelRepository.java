
package SeatSync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import SeatSync.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Search hotels by name or city
    List<Hotel> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
            String name, String city);

    // Lock hotel while creating a booking
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Hotel h WHERE h.id = :id")
    Optional<Hotel> findByIdForUpdate(@Param("id") Long id);
}
