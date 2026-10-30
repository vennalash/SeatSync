
package SeatSync.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SeatSync.entity.Event;
import SeatSync.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Customer: Get active events
    public List<Event> getActiveEvents() {
        return eventRepository.findAll()
                .stream()
                .filter(Event::isActive)
                .toList();
    }

    // Customer: Get one active event
    public Event getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));

        if (!event.isActive()) {
            throw new RuntimeException("This event is not available");
        }

        return event;
    }

    // Admin: Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Admin: Search events
    public List<Event> searchEvents(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllEvents();
        }

        return eventRepository
                .findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                        keyword.trim(), keyword.trim());
    }

    // Admin: Add event
    @Transactional
    public Event addEvent(Event event) {

        validateEvent(event);

        event.setId(null);
        event.setActive(true);

        return eventRepository.save(event);
    }

    // Admin: Update event
    @Transactional
    public Event updateEvent(Long id, Event updatedEvent) {

        validateEvent(updatedEvent);
        validateTickets(updatedEvent);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));

        existingEvent.setName(updatedEvent.getName().trim());
        existingEvent.setCategory(updatedEvent.getCategory().trim());
        existingEvent.setVenue(updatedEvent.getVenue().trim());
        existingEvent.setCity(updatedEvent.getCity().trim());
        existingEvent.setEventDate(updatedEvent.getEventDate());
        existingEvent.setTicketPrice(updatedEvent.getTicketPrice());

        // FIX: Update ticket inventory
        existingEvent.setTotalTickets(updatedEvent.getTotalTickets());
        existingEvent.setAvailableTickets(updatedEvent.getAvailableTickets());

        // FIX: Update event image
        existingEvent.setImageUrl(updatedEvent.getImageUrl());

        return eventRepository.save(existingEvent);
    }

    // Admin: Deactivate event
    @Transactional
    public Event deactivateEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));

        event.setActive(false);

        return eventRepository.save(event);
    }

    // Admin: Activate event
    @Transactional
    public Event activateEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));

        event.setActive(true);

        return eventRepository.save(event);
    }

    // Validate ticket quantities
    private void validateTickets(Event event) {

        if (event.getTotalTickets() == null ||
                event.getTotalTickets() < 1) {
            throw new RuntimeException(
                    "Total tickets must be at least 1");
        }

        if (event.getAvailableTickets() == null ||
                event.getAvailableTickets() < 0) {
            throw new RuntimeException(
                    "Available tickets cannot be negative");
        }

        if (event.getAvailableTickets() > event.getTotalTickets()) {
            throw new RuntimeException(
                    "Available tickets cannot exceed total tickets");
        }
    }

    // Validation
    private void validateEvent(Event event) {

        if (event.getName() == null || event.getName().isBlank()) {
            throw new RuntimeException("Event name is required");
        }

        if (event.getCategory() == null || event.getCategory().isBlank()) {
            throw new RuntimeException("Event category is required");
        }

        if (event.getVenue() == null || event.getVenue().isBlank()) {
            throw new RuntimeException("Event venue is required");
        }

        if (event.getCity() == null || event.getCity().isBlank()) {
            throw new RuntimeException("Event city is required");
        }

        if (event.getEventDate() == null || event.getEventDate().isBlank()) {
            throw new RuntimeException("Event date is required");
        }

        if (event.getTicketPrice() == null ||
                event.getTicketPrice() <= 0) {
            throw new RuntimeException("Enter a valid ticket price");
        }
    }
}
