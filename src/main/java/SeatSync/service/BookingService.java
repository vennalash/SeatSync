
package SeatSync.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SeatSync.entity.Booking;
import SeatSync.entity.BookingStatus;
import SeatSync.entity.Event;
import SeatSync.entity.Hotel;
import SeatSync.entity.MovieShow;
import SeatSync.entity.SportsFacility;

import SeatSync.repository.BookingRepository;
import SeatSync.repository.MovieRepository;
import SeatSync.repository.MovieShowRepository;
import SeatSync.repository.EventRepository;
import SeatSync.repository.HotelRepository;
import SeatSync.repository.SportsFacilityRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final MovieShowRepository movieShowRepository;
    private final EventRepository eventRepository;
    private final HotelRepository hotelRepository;
    private final SportsFacilityRepository sportsFacilityRepository;

    public BookingService(
            BookingRepository bookingRepository,
            MovieRepository movieRepository,
            MovieShowRepository movieShowRepository,
            EventRepository eventRepository,
            HotelRepository hotelRepository,
            SportsFacilityRepository sportsFacilityRepository) {

        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.movieShowRepository = movieShowRepository;
        this.eventRepository = eventRepository;
        this.hotelRepository = hotelRepository;
        this.sportsFacilityRepository = sportsFacilityRepository;
    }

    // CREATE BOOKING
    @Transactional
    public Booking createBooking(Booking booking) {

        if (booking == null || booking.getServiceType() == null
                || booking.getServiceId() == null) {
            throw new RuntimeException("Select a valid service");
        }

        String type = booking.getServiceType().toUpperCase();
        Long id = booking.getServiceId();
        double price;

        switch (type) {

            // MOVIE BOOKING
            case "MOVIE":

                if (booking.getShowId() == null) {
                    throw new RuntimeException(
                            "Please select a cinema show");
                }

                MovieShow show = movieShowRepository
                        .findByIdForUpdate(booking.getShowId())
                        .orElseThrow(() ->
                                new RuntimeException("Cinema show not found"));

                if (show.getMovie() == null
                        || !id.equals(show.getMovie().getId())) {
                    throw new RuntimeException(
                            "Selected show does not belong to this movie");
                }

                if (show.getTicketPrice() == null
                        || show.getTicketPrice() <= 0) {
                    throw new RuntimeException("Invalid show ticket price");
                }

                price = show.getTicketPrice();
                break;

            // EVENT BOOKING
            case "EVENT":

                Integer quantity = booking.getTicketQuantity();

                if (quantity == null) {
                    quantity = 1;
                }

                if (quantity < 1 || quantity > 10) {
                    throw new RuntimeException(
                            "You can book between 1 and 10 event tickets");
                }

                // Lock event record to prevent overselling
                Event event = eventRepository.findByIdForUpdate(id)
                        .orElseThrow(() ->
                                new RuntimeException("Event not found"));

                if (!event.isActive()) {
                    throw new RuntimeException(
                            "This event is not available");
                }

                if (event.getEventDate() != null
                        && LocalDate.parse(event.getEventDate())
                                .isBefore(LocalDate.now())) {
                    throw new RuntimeException(
                            "This event has already passed");
                }

                if (event.getTicketPrice() == null
                        || event.getTicketPrice() <= 0) {
                    throw new RuntimeException(
                            "Invalid event ticket price");
                }

                if (event.getAvailableTickets() == null
                        || event.getAvailableTickets() < quantity) {
                    throw new RuntimeException(
                            "Not enough tickets available");
                }

                booking.setTicketQuantity(quantity);

                price = event.getTicketPrice() * quantity;

                // Deduct available tickets
                event.setAvailableTickets(
                        event.getAvailableTickets() - quantity);

                eventRepository.save(event);

                break;

            // HOTEL BOOKING
            case "HOTEL":

                Hotel hotel = hotelRepository.findByIdForUpdate(id)
                        .orElseThrow(() ->
                                new RuntimeException("Hotel not found"));

                if (!hotel.isActive()) {
                    throw new RuntimeException(
                            "This hotel is not available");
                }

                LocalDate checkIn = booking.getCheckInDate();
                LocalDate checkOut = booking.getCheckOutDate();

                if (checkIn == null || checkOut == null) {
                    throw new RuntimeException(
                            "Please select check-in and check-out dates");
                }

                if (checkIn.isBefore(LocalDate.now())) {
                    throw new RuntimeException(
                            "Check-in date cannot be in the past");
                }

                if (!checkOut.isAfter(checkIn)) {
                    throw new RuntimeException(
                            "Check-out must be after check-in");
                }

                Integer rooms = booking.getRoomQuantity();
                Integer guests = booking.getGuestCount();

                if (rooms == null || rooms < 1) {
                    throw new RuntimeException(
                            "Please select at least one room");
                }

                if (guests == null || guests < 1) {
                    throw new RuntimeException(
                            "Please enter the number of guests");
                }

                if (guests < rooms) {
                    throw new RuntimeException(
                            "At least one guest is required per room");
                }

                if (hotel.getAvailableRooms() == null
                        || hotel.getAvailableRooms() < rooms) {
                    throw new RuntimeException(
                            "Not enough rooms available");
                }

                if (hotel.getPricePerNight() == null
                        || hotel.getPricePerNight() <= 0) {
                    throw new RuntimeException("Invalid hotel price");
                }

                long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

                price = hotel.getPricePerNight() * nights * rooms;

                booking.setRoomQuantity(rooms);
                booking.setGuestCount(guests);

                // Reserve hotel rooms
                hotel.setAvailableRooms(
                        hotel.getAvailableRooms() - rooms);

                hotelRepository.save(hotel);

                break;

            // SPORTS BOOKING
            case "SPORTS":

                SportsFacility facility = sportsFacilityRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sports facility not found"));

                if (!facility.isActive()) {
                    throw new RuntimeException(
                            "This sports facility is not available");
                }

                if (facility.getPricePerHour() == null
                        || facility.getPricePerHour() <= 0) {
                    throw new RuntimeException(
                            "Invalid sports facility price");
                }

                LocalDate sportsDate = booking.getSportsDate();
                LocalTime sportsStartTime = booking.getSportsStartTime();
                Integer duration = booking.getSportsDurationHours();

                if (sportsDate == null || sportsStartTime == null) {
                    throw new RuntimeException(
                            "Please select sports date and start time");
                }

                if (sportsDate.isBefore(LocalDate.now())) {
                    throw new RuntimeException(
                            "Sports booking date cannot be in the past");
                }

                if (duration == null || duration < 1 || duration > 12) {
                    throw new RuntimeException(
                            "Duration must be between 1 and 12 hours");
                }

                if (sportsDate.equals(LocalDate.now())
                        && !sportsStartTime.isAfter(LocalTime.now())) {
                    throw new RuntimeException(
                            "Please select a future booking time");
                }

                LocalTime sportsEndTime =
                        sportsStartTime.plusHours(duration);

                if (!sportsEndTime.isAfter(sportsStartTime)) {
                    throw new RuntimeException("Invalid booking time");
                }

                List<Booking> existingSportsBookings =
                        bookingRepository
                                .findByServiceTypeAndServiceIdAndSportsDateAndStatusIn(
                                        "SPORTS",
                                        id,
                                        sportsDate,
                                        List.of(
                                                BookingStatus.PENDING,
                                                BookingStatus.CONFIRMED
                                        )
                                );

                for (Booking existing : existingSportsBookings) {

                    if (existing.getSportsStartTime() == null
                            || existing.getSportsDurationHours() == null) {
                        continue;
                    }

                    LocalTime existingStart =
                            existing.getSportsStartTime();

                    LocalTime existingEnd = existingStart.plusHours(
                            existing.getSportsDurationHours());

                    boolean overlap =
                            sportsStartTime.isBefore(existingEnd)
                            && sportsEndTime.isAfter(existingStart);

                    if (overlap) {
                        throw new RuntimeException(
                                "This sports facility is already booked "
                                + "for the selected time");
                    }
                }

                booking.setSportsDate(sportsDate);
                booking.setSportsStartTime(sportsStartTime);
                booking.setSportsDurationHours(duration);

                price = facility.getPricePerHour() * duration;

                break;

            default:
                throw new RuntimeException("Invalid service type");
        }

        booking.setServiceType(type);

        // Clear unrelated sports details for other booking types
        if (!"SPORTS".equals(type)) {
            booking.setSportsDate(null);
            booking.setSportsStartTime(null);
            booking.setSportsDurationHours(null);
        }

        // MOVIE SEAT VALIDATION
        if ("MOVIE".equals(type)) {

            if (booking.getSeats() == null
                    || booking.getSeats().isBlank()) {
                throw new RuntimeException("Please select seats");
            }

            String[] seats = booking.getSeats().split(",");

            for (int i = 0; i < seats.length; i++) {
                seats[i] = seats[i].trim();
            }

            if (seats.length > 6) {
                throw new RuntimeException("Maximum 6 seats allowed");
            }

            if (Arrays.stream(seats).anyMatch(
                    seat -> !seat.matches("[A-F][1-8]"))) {
                throw new RuntimeException("Invalid seat selection");
            }

            if (Arrays.stream(seats).distinct().count() != seats.length) {
                throw new RuntimeException(
                        "Duplicate seats are not allowed");
            }

            List<Booking> existingBookings =
                    bookingRepository.findByShowIdAndServiceTypeAndStatusIn(
                            booking.getShowId(),
                            "MOVIE",
                            List.of(
                                    BookingStatus.PENDING,
                                    BookingStatus.CONFIRMED
                            )
                    );

            for (Booking existing : existingBookings) {

                if (existing.getSeats() == null) {
                    continue;
                }

                String[] bookedSeats = existing.getSeats().split(",");

                for (String selectedSeat : seats) {
                    for (String bookedSeat : bookedSeats) {

                        if (selectedSeat.equals(bookedSeat.trim())) {
                            throw new RuntimeException(
                                    "Seat " + selectedSeat
                                    + " is already booked for this show.");
                        }
                    }
                }
            }

            booking.setSeats(String.join(",", seats));
            price = price * seats.length;

        } else {
            booking.setShowId(null);
        }

        // HOTEL COUPON DISCOUNT
        if ("HOTEL".equals(type)) {

            String coupon = booking.getCouponCode();

            double discount = calculateDiscount(coupon, price);

            booking.setCouponCode(
                    coupon == null || coupon.isBlank()
                            ? null
                            : coupon.trim().toUpperCase()
            );

            booking.setOriginalAmount(price);
            booking.setDiscountAmount(discount);
            booking.setTotalAmount(price - discount);

        } else {

            // No coupons for Movie, Event or Sports
            booking.setCouponCode(null);
            booking.setOriginalAmount(price);
            booking.setDiscountAmount(0.0);
            booking.setTotalAmount(price);
        }

        booking.setBookingReference(
                "SS-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());

        booking.setStatus(BookingStatus.PENDING);
        booking.setBookingTime(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    // CALCULATE COUPON DISCOUNT
    private double calculateDiscount(String couponCode, double price) {

        if (couponCode == null || couponCode.isBlank()) {
            return 0.0;
        }

        String code = couponCode.trim().toUpperCase();

        switch (code) {

            case "WELCOME10":
                return price * 0.10;

            case "HOTEL20":
                return price * 0.20;

            default:
                throw new RuntimeException("Invalid coupon code");
        }
    }

    // GET BOOKING
    public Booking getBooking(String reference) {

        return bookingRepository.findByBookingReference(reference)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));
    }

    // CONFIRM BOOKING AFTER SUCCESSFUL DEMO PAYMENT
    @Transactional
    public Booking confirmDemoPayment(String reference) {

        Booking booking = getBooking(reference);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException(
                    "Cancelled booking cannot be confirmed");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending bookings can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    // CANCEL BOOKING AND RESTORE ROOMS / EVENT TICKETS
    @Transactional
    public Booking cancelBooking(String reference) {

        Booking booking = getBooking(reference);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED
                && booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException(
                    "This booking cannot be cancelled");
        }

        // RESTORE HOTEL ROOMS
        if ("HOTEL".equalsIgnoreCase(booking.getServiceType())
                && booking.getRoomQuantity() != null) {

            Hotel hotel = hotelRepository
                    .findByIdForUpdate(booking.getServiceId())
                    .orElseThrow(() ->
                            new RuntimeException("Hotel not found"));

            int currentRooms = hotel.getAvailableRooms() == null
                    ? 0 : hotel.getAvailableRooms();

            hotel.setAvailableRooms(
                    currentRooms + booking.getRoomQuantity());

            hotelRepository.save(hotel);
        }

        // RESTORE EVENT TICKETS
        if ("EVENT".equalsIgnoreCase(booking.getServiceType())
                && booking.getTicketQuantity() != null) {

            Event event = eventRepository
                    .findByIdForUpdate(booking.getServiceId())
                    .orElseThrow(() ->
                            new RuntimeException("Event not found"));

            int available = event.getAvailableTickets() == null
                    ? 0 : event.getAvailableTickets();

            int restoredTickets =
                    available + booking.getTicketQuantity();

            if (event.getTotalTickets() != null) {
                restoredTickets = Math.min(
                        restoredTickets,
                        event.getTotalTickets());
            }

            event.setAvailableTickets(restoredTickets);

            eventRepository.save(event);
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepository.save(booking);
    }

    // GET BOOKED SEATS FOR A PARTICULAR CINEMA SHOW
    public List<String> getBookedSeatsByShow(Long showId) {

        List<BookingStatus> activeStatuses = List.of(
                BookingStatus.PENDING,
                BookingStatus.CONFIRMED
        );

        return bookingRepository.findBookedSeatsByShow(
                        showId, activeStatuses)
                .stream()
                .flatMap(seats -> Arrays.stream(seats.split(",")))
                .map(String::trim)
                .filter(seat -> !seat.isEmpty())
                .distinct()
                .toList();
    }

    // GET ALL BOOKINGS
    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();
    }

    // CUSTOMER BOOKING HISTORY
    public List<Booking> getBookingsByCustomerName(String customerName) {

        if (customerName == null || customerName.isBlank()) {
            throw new RuntimeException("Customer name is required");
        }

        return bookingRepository
                .findByCustomerNameIgnoreCase(customerName.trim());
    }

    // ADMIN SEARCH AND FILTER
    public List<Booking> searchAdminBookings(
            String keyword,
            String serviceType,
            String status) {

        return bookingRepository.findAll().stream()

                .filter(booking -> {
                    if (keyword == null || keyword.isBlank()) {
                        return true;
                    }

                    String search = keyword.trim().toLowerCase();

                    boolean referenceMatch =
                            booking.getBookingReference() != null &&
                            booking.getBookingReference()
                                    .toLowerCase().contains(search);

                    boolean customerMatch =
                            booking.getCustomerName() != null &&
                            booking.getCustomerName()
                                    .toLowerCase().contains(search);

                    return referenceMatch || customerMatch;
                })

                .filter(booking ->
                        serviceType == null || serviceType.isBlank() ||
                        (booking.getServiceType() != null &&
                        booking.getServiceType().equalsIgnoreCase(
                                serviceType.trim())))

                .filter(booking ->
                        status == null || status.isBlank() ||
                        (booking.getStatus() != null &&
                        booking.getStatus().name().equalsIgnoreCase(
                                status.trim())))

                .toList();
    }
}
