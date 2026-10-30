package SeatSync.repository;

import SeatSync.entity.SportsFacility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsFacilityRepository
        extends JpaRepository<SportsFacility, Long> {
}