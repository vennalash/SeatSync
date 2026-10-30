package SeatSync.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import SeatSync.entity.User;
import SeatSync.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register user
    @Transactional
    public User registerUser(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            throw new RuntimeException("Name is required");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        String email = user.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        // Never accept an ADMIN role from public registration
        user.setRole("CUSTOMER");
        user.setName(user.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Login user
    public boolean loginUser(String email, String password) {

        if (email == null || password == null) {
            return false;
        }

        User user = userRepository.findByEmail(
                email.trim().toLowerCase()
        ).orElse(null);

        if (user == null || user.getPassword() == null) {
            return false;
        }

        return passwordEncoder.matches(
                password,
                user.getPassword()
        );
    }

    // Get user by email
    public User getUserByEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email is required");
        }

        return userRepository.findByEmail(
                email.trim().toLowerCase()
        ).orElseThrow(() ->
                new RuntimeException("User not found"));
    }

    // Get user by ID
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // Update profile name and email
    @Transactional
    public User updateUser(User updatedUser) {

        User existingUser = getUserById(updatedUser.getId());

        if (updatedUser.getName() == null
                || updatedUser.getName().isBlank()) {
            throw new RuntimeException("Name cannot be empty");
        }

        if (updatedUser.getEmail() == null
                || updatedUser.getEmail().isBlank()) {
            throw new RuntimeException("Email cannot be empty");
        }

        String newEmail = updatedUser.getEmail()
                .trim()
                .toLowerCase();

        User emailUser = userRepository.findByEmail(newEmail)
                .orElse(null);

        if (emailUser != null
                && !emailUser.getId().equals(existingUser.getId())) {
            throw new RuntimeException("Email already registered");
        }

        existingUser.setName(updatedUser.getName().trim());
        existingUser.setEmail(newEmail);

        // Preserve the existing role and password
        return userRepository.save(existingUser);
    }

    // Check whether a user is an admin
    public boolean isAdmin(Long userId) {

        User user = getUserById(userId);

        return "ADMIN".equalsIgnoreCase(user.getRole());
    }
    

 // Get all users for admin
 public List<User> getAllUsers() {
     return userRepository.findAll();
 }

 // Search users by name or email
 public List<User> searchUsers(String keyword) {

     if (keyword == null || keyword.isBlank()) {
         return getAllUsers();
     }

     String search = keyword.trim().toLowerCase();

     return userRepository.findAll().stream()
             .filter(user ->
                     (user.getName() != null &&
                      user.getName().toLowerCase().contains(search))
                     ||
                     (user.getEmail() != null &&
                      user.getEmail().toLowerCase().contains(search))
             )
             .collect(Collectors.toList());
 }

}