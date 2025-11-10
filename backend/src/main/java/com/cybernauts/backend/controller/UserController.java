package com.cybernauts.backend.controller;

import com.cybernauts.backend.dto.UserDTO;
import com.cybernauts.backend.model.User;
import com.cybernauts.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // -------------------- CRUD --------------------

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("GET /api/users - Fetch all users");
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(userService::toDTO)
                .collect(Collectors.toList());
        log.debug("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        log.info("GET /api/users/{} - Fetch user by ID", id);
        User user = userService.getUserById(id);
        log.debug("User found: {}", user.getUsername());
        return ResponseEntity.ok(userService.toDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User user) {
        log.info("POST /api/users - Create new user: {}", user.getUsername());
        User createdUser = userService.createUser(user);
        log.debug("User created with ID: {}", createdUser.getId());
        return ResponseEntity.ok(userService.toDTO(createdUser));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        log.info("PUT /api/users/{} - Update user", id);
        User updatedUser = userService.updateUser(id, user);
        log.debug("User updated successfully: {}", id);
        return ResponseEntity.ok(userService.toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        log.info("DELETE /api/users/{} - Delete user", id);
        userService.deleteUser(id);
        log.debug("User deleted successfully: {}", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // -------------------- FRIENDSHIP --------------------

    @PostMapping("/{id}/link")
    public ResponseEntity<String> linkFriend(@PathVariable UUID id, @RequestParam(required = true) UUID friendId) {
        log.info("POST /api/users/{}/link - Link users {} and {}", id, id, friendId);
        userService.linkUsers(id, friendId);
        log.debug("Users linked successfully: {} and {}", id, friendId);
        return ResponseEntity.ok("Users linked successfully");
    }

    @DeleteMapping("/{id}/unlink")
    public ResponseEntity<String> unlinkFriend(@PathVariable UUID id, @RequestParam(required = true) UUID friendId) {
        log.info("DELETE /api/users/{}/unlink - Unlink users {} and {}", id, id, friendId);
        userService.unlinkUsers(id, friendId);
        log.debug("Users unlinked successfully: {} and {}", id, friendId);
        return ResponseEntity.ok("Users unlinked successfully");
    }

    // -------------------- GRAPH DATA --------------------

    @GetMapping("/graph")
    public ResponseEntity<Map<String, Object>> getGraphData() {
        log.info("GET /api/users/graph - Fetch graph data");
        Map<String, Object> graph = userService.getGraphData();
        log.debug("Graph data fetched successfully");
        return ResponseEntity.ok(graph);
    }
}
