package SeatSync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import SeatSync.entity.Hotel;
import SeatSync.service.HotelService;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // Customer: Get active hotels
    @GetMapping
    public List<Hotel> getActiveHotels() {
        return hotelService.getActiveHotels();
    }

    // Admin: Get all hotels
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    // Admin: Search hotels
    @GetMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Hotel> searchHotels(
            @RequestParam String keyword) {
        return hotelService.searchHotels(keyword);
    }

    // Admin: Add hotel
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> addHotel(
            @RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.addHotel(hotel));
    }

    // Admin: Update hotel
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(
            @PathVariable Long id,
            @RequestBody Hotel hotel) {
        return ResponseEntity.ok(
                hotelService.updateHotel(id, hotel));
    }

    // Admin: Deactivate hotel
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> deactivateHotel(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                hotelService.deactivateHotel(id));
    }

    // Admin: Activate hotel
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> activateHotel(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                hotelService.activateHotel(id));
    }
}