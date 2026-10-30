
package SeatSync.service;

import java.util.List;

import org.springframework.stereotype.Service;

import SeatSync.entity.SportsFacility;
import SeatSync.repository.SportsFacilityRepository;

@Service
public class SportsFacilityService {

    private final SportsFacilityRepository sportsFacilityRepository;

    public SportsFacilityService(
            SportsFacilityRepository sportsFacilityRepository) {
        this.sportsFacilityRepository = sportsFacilityRepository;
    }

    // Customer: get active facilities only
    public List<SportsFacility> getActiveFacilities() {
        return sportsFacilityRepository.findAll()
                .stream()
                .filter(SportsFacility::isActive)
                .toList();
    }

    // Admin: get all facilities
    public List<SportsFacility> getAllFacilities() {
        return sportsFacilityRepository.findAll();
    }

    // Admin: add facility
    public SportsFacility addFacility(SportsFacility facility) {

        validateFacility(facility);

        facility.setId(null);
        facility.setActive(true);

        return sportsFacilityRepository.save(facility);
    }

    // Admin: update facility
    public SportsFacility updateFacility(Long id, SportsFacility details) {

        validateFacility(details);

        SportsFacility facility = sportsFacilityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sports facility not found"));

        facility.setName(details.getName().trim());
        facility.setSportType(details.getSportType().trim());
        facility.setCity(details.getCity().trim());
        facility.setAddress(details.getAddress().trim());
        facility.setPricePerHour(details.getPricePerHour());

        return sportsFacilityRepository.save(facility);
    }

    // Admin: deactivate facility
    public SportsFacility deactivateFacility(Long id) {

        SportsFacility facility = sportsFacilityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sports facility not found"));

        facility.setActive(false);

        return sportsFacilityRepository.save(facility);
    }

    // Admin: activate facility
    public SportsFacility activateFacility(Long id) {

        SportsFacility facility = sportsFacilityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sports facility not found"));

        facility.setActive(true);

        return sportsFacilityRepository.save(facility);
    }

    // Search facilities
    public List<SportsFacility> searchFacilities(String keyword) {

        String search = keyword == null ? "" :
                keyword.trim().toLowerCase();

        return sportsFacilityRepository.findAll()
                .stream()
                .filter(facility ->
                        contains(facility.getName(), search) ||
                        contains(facility.getSportType(), search) ||
                        contains(facility.getCity(), search))
                .toList();
    }

    private boolean contains(String value, String search) {
        return value != null &&
                value.toLowerCase().contains(search);
    }

    private void validateFacility(SportsFacility facility) {

        if (facility.getName() == null ||
                facility.getName().isBlank()) {
            throw new RuntimeException("Facility name is required");
        }

        if (facility.getSportType() == null ||
                facility.getSportType().isBlank()) {
            throw new RuntimeException("Sport type is required");
        }

        if (facility.getCity() == null ||
                facility.getCity().isBlank()) {
            throw new RuntimeException("City is required");
        }

        if (facility.getAddress() == null ||
                facility.getAddress().isBlank()) {
            throw new RuntimeException("Address is required");
        }

        if (facility.getPricePerHour() == null ||
                facility.getPricePerHour() <= 0) {
            throw new RuntimeException("Enter a valid hourly price");
        }
    }
}
