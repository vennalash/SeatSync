
package SeatSync.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import SeatSync.entity.Booking;
import SeatSync.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody Booking booking) {
        return ResponseEntity.ok(
                bookingService.createBooking(booking));
    }

    // Get booking by reference
    @GetMapping("/{reference}")
    public ResponseEntity<Booking> getBooking(
            @PathVariable String reference) {
        return ResponseEntity.ok(
                bookingService.getBooking(reference));
    }

    // Cancel booking
    @PutMapping("/{reference}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable String reference) {
        return ResponseEntity.ok(
                bookingService.cancelBooking(reference));
    }

    // Confirm demo payment
    @PutMapping("/{reference}/confirm-demo")
    public ResponseEntity<Booking> confirmDemoPayment(
            @PathVariable String reference) {
        return ResponseEntity.ok(
                bookingService.confirmDemoPayment(reference));
    }

    // Get booked seats for a cinema show
    @GetMapping("/show/{showId}/booked-seats")
    public ResponseEntity<Map<String, List<String>>> getBookedSeatsByShow(
            @PathVariable Long showId) {

        List<String> seats =
                bookingService.getBookedSeatsByShow(showId);

        return ResponseEntity.ok(
                Map.of("bookedSeats", seats));
    }

    // Customer booking history
    @GetMapping("/history")
    public List<Booking> getBookingHistory(
            @RequestParam String customerName) {
        return bookingService.getBookingsByCustomerName(customerName);
    }

    // ADMIN: Get all bookings
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(
                bookingService.getAllBookings());
    }

    // ADMIN: Search and filter bookings
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/search")
    public ResponseEntity<List<Booking>> searchBookings(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(
                bookingService.searchAdminBookings(
                        keyword, serviceType, status));
    }

    // ADMIN: Cancel a booking
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{reference}/cancel")
    public ResponseEntity<Booking> adminCancelBooking(
            @PathVariable String reference) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(reference));
    }
}
