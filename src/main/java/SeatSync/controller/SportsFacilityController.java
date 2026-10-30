
package SeatSync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SeatSync.entity.SportsFacility;
import SeatSync.service.SportsFacilityService;

@RestController
@RequestMapping("/api/sports")
public class SportsFacilityController {

    private final SportsFacilityService sportsFacilityService;

    public SportsFacilityController(
            SportsFacilityService sportsFacilityService) {
        this.sportsFacilityService = sportsFacilityService;
    }

    // Customer: active facilities
    @GetMapping
    public List<SportsFacility> getActiveFacilities() {
        return sportsFacilityService.getActiveFacilities();
    }

    // Admin: all facilities
    @GetMapping("/admin/all")
    public List<SportsFacility> getAllFacilities() {
        return sportsFacilityService.getAllFacilities();
    }

    // Admin: search
    @GetMapping("/admin/search")
    public List<SportsFacility> searchFacilities(
            @RequestParam String keyword) {
        return sportsFacilityService.searchFacilities(keyword);
    }

    // Admin: add
    @PostMapping
    public ResponseEntity<SportsFacility> addFacility(
            @RequestBody SportsFacility facility) {
        return ResponseEntity.ok(
                sportsFacilityService.addFacility(facility));
    }

    // Admin: update
    @PutMapping("/{id}")
    public ResponseEntity<SportsFacility> updateFacility(
            @PathVariable Long id,
            @RequestBody SportsFacility facility) {
        return ResponseEntity.ok(
                sportsFacilityService.updateFacility(id, facility));
    }

    // Admin: deactivate
    @DeleteMapping("/{id}")
    public ResponseEntity<SportsFacility> deactivateFacility(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                sportsFacilityService.deactivateFacility(id));
    }

    // Admin: reactivate
    @PutMapping("/{id}/activate")
    public ResponseEntity<SportsFacility> activateFacility(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                sportsFacilityService.activateFacility(id));
    }
}
