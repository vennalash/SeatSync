package SeatSync.service;

import java.util.List;

import org.springframework.stereotype.Service;

import SeatSync.entity.Hotel;
import SeatSync.repository.HotelRepository;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    // Get active hotels for customers
    public List<Hotel> getActiveHotels() {
        return hotelRepository.findAll()
                .stream()
                .filter(Hotel::isActive)
                .toList();
    }

    // Get all hotels for admin
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    // Search hotels
    public List<Hotel> searchHotels(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllHotels();
        }

        return hotelRepository
                .findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
                        keyword.trim(), keyword.trim());
    }

    // Add hotel
    public Hotel addHotel(Hotel hotel) {

        validateHotel(hotel);

        hotel.setId(null);
        hotel.setActive(true);

        return hotelRepository.save(hotel);
    }

    // Update hotel
    public Hotel updateHotel(Long id, Hotel updatedHotel) {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        validateHotel(updatedHotel);

        hotel.setName(updatedHotel.getName());
        hotel.setCity(updatedHotel.getCity());
        hotel.setAddress(updatedHotel.getAddress());
        hotel.setPricePerNight(updatedHotel.getPricePerNight());
        hotel.setAvailableRooms(updatedHotel.getAvailableRooms());
        hotel.setDescription(updatedHotel.getDescription());

        return hotelRepository.save(hotel);
    }

    // Deactivate hotel
    public Hotel deactivateHotel(Long id) {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        hotel.setActive(false);

        return hotelRepository.save(hotel);
    }

    // Activate hotel
    public Hotel activateHotel(Long id) {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        hotel.setActive(true);

        return hotelRepository.save(hotel);
    }

    // Validate hotel details
    private void validateHotel(Hotel hotel) {

        if (hotel.getName() == null || hotel.getName().isBlank()) {
            throw new RuntimeException("Hotel name is required");
        }

        if (hotel.getCity() == null || hotel.getCity().isBlank()) {
            throw new RuntimeException("City is required");
        }

        if (hotel.getAddress() == null || hotel.getAddress().isBlank()) {
            throw new RuntimeException("Address is required");
        }

        if (hotel.getPricePerNight() == null
                || hotel.getPricePerNight() <= 0) {
            throw new RuntimeException("Price must be greater than zero");
        }

        if (hotel.getAvailableRooms() == null
                || hotel.getAvailableRooms() < 0) {
            throw new RuntimeException("Available rooms cannot be negative");
        }
    }
}