
package SeatSync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import SeatSync.entity.Event;
import SeatSync.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // CUSTOMER: Get active events
    @GetMapping
    public List<Event> getActiveEvents() {
        return eventService.getActiveEvents();
    }

    // CUSTOMER: Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(
            @PathVariable Long id) {

        return ResponseEntity.ok(eventService.getEventById(id));
    }

    // ADMIN: Get all events
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // ADMIN: Search events
    @GetMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Event> searchEvents(
            @RequestParam(required = false) String keyword) {

        return eventService.searchEvents(keyword);
    }

    // ADMIN: Add event
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> addEvent(
            @RequestBody Event event) {

        return ResponseEntity.ok(eventService.addEvent(event));
    }

    // ADMIN: Update event
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody Event event) {

        return ResponseEntity.ok(
                eventService.updateEvent(id, event));
    }

    // ADMIN: Deactivate event
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> deactivateEvent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                eventService.deactivateEvent(id));
    }

    // ADMIN: Activate event
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> activateEvent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                eventService.activateEvent(id));
    }
}
