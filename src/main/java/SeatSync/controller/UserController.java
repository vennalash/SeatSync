
package SeatSync.controller;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import SeatSync.entity.User;
import SeatSync.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SecurityContextRepository securityContextRepository;

    public UserController(UserService userService) {
        this.userService = userService;
        this.securityContextRepository =
                new HttpSessionSecurityContextRepository();
    }

    // Register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);

            return ResponseEntity.ok(Map.of(
                "id", savedUser.getId(),
                "name", savedUser.getName(),
                "email", savedUser.getEmail(),
                "message", "Registration successful"
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
            );
        }
    }

    // Login user and create authenticated session
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody User user,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            boolean success = userService.loginUser(
                user.getEmail(),
                user.getPassword()
            );

            if (!success) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "message", "Invalid email or password"
                    ));
            }

            User loggedInUser =
                userService.getUserByEmail(user.getEmail());

            String role = loggedInUser.getRole();

            if (role == null || role.isBlank()) {
                role = "CUSTOMER";
            }

            // Only trust the role stored in the database
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    loggedInUser.getEmail(),
                    null,
                    java.util.List.of(
                        new SimpleGrantedAuthority("ROLE_" + role)
                    )
                );

            SecurityContext context =
                SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // Save authentication in the HTTP session
            securityContextRepository.saveContext(
                context, request, response
            );

            return ResponseEntity.ok(Map.of(
                "id", loggedInUser.getId(),
                "name", loggedInUser.getName(),
                "email", loggedInUser.getEmail(),
                "role", role,
                "message", "Login successful"
            ));

        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                    "message", "Invalid email or password"
                ));
        }
    }

    // Logout user
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(
            HttpServletRequest request) {

        SecurityContextHolder.clearContext();

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        return ResponseEntity.ok(
            Map.of("message", "Logout successful")
        );
    }

    // View profile
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);

            return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
            );
        }
    }

    // Update profile
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody User updatedUser) {

        try {
            if (updatedUser.getName() == null
                    || updatedUser.getName().isBlank()
                    || updatedUser.getEmail() == null
                    || updatedUser.getEmail().isBlank()) {

                return ResponseEntity.badRequest().body(
                    Map.of("message", "Name and email are required")
                );
            }

            User user = userService.getUserById(id);

            user.setName(updatedUser.getName().trim());
            user.setEmail(updatedUser.getEmail().trim());

            User savedUser = userService.updateUser(user);

            return ResponseEntity.ok(Map.of(
                "id", savedUser.getId(),
                "name", savedUser.getName(),
                "email", savedUser.getEmail(),
                "message", "Profile updated successfully"
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
            );
        }
    }

 // ADMIN: Get all users without exposing passwords
 @PreAuthorize("hasRole('ADMIN')")
 @GetMapping("/admin/all")
 public ResponseEntity<List<Map<String, Object>>> getAllUsers() {

     List<Map<String, Object>> users =
             userService.getAllUsers().stream()
                     .map(user -> Map.<String, Object>of(
                             "id", user.getId(),
                             "name", user.getName(),
                             "email", user.getEmail(),
                             "role", user.getRole()
                     ))
                     .collect(Collectors.toList());

     return ResponseEntity.ok(users);
 }

 // ADMIN: Search users by name or email
 @PreAuthorize("hasRole('ADMIN')")
 @GetMapping("/admin/search")
 public ResponseEntity<List<Map<String, Object>>> searchUsers(
         @RequestParam(required = false) String keyword) {

     List<Map<String, Object>> users =
             userService.searchUsers(keyword).stream()
                     .map(user -> Map.<String, Object>of(
                             "id", user.getId(),
                             "name", user.getName(),
                             "email", user.getEmail(),
                             "role", user.getRole()
                     ))
                     .collect(Collectors.toList());

     return ResponseEntity.ok(users);
 }

}
