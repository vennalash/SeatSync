
package SeatSync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import SeatSync.entity.MovieShow;

public interface MovieShowRepository
        extends JpaRepository<MovieShow, Long> {

    List<MovieShow> findByMovieId(Long movieId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM MovieShow s WHERE s.id = :showId")
    Optional<MovieShow> findByIdForUpdate(
            @Param("showId") Long showId);
}
