package com.cybernauts.backend.controller;

import com.cybernauts.backend.dto.UserDTO;
import com.cybernauts.backend.model.User;
import com.cybernauts.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(userService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(userService.toDTO(createdUser));
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // -------------------- FRIENDSHIP --------------------

    @PostMapping("/{id}/link")
    public ResponseEntity<String> linkFriend(@PathVariable UUID id, @RequestParam UUID friendId) {
        userService.linkUsers(id, friendId);
        return ResponseEntity.ok("Users linked successfully");
    }

    @DeleteMapping("/{id}/unlink")
    public ResponseEntity<String> unlinkFriend(@PathVariable UUID id, @RequestParam UUID friendId) {
        userService.unlinkUsers(id, friendId);
        return ResponseEntity.ok("Users unlinked successfully");
    }

    // -------------------- GRAPH DATA --------------------

    @GetMapping("/graph")
    public ResponseEntity<Map<String, Object>> getGraphData() {
        Map<String, Object> graph = userService.getGraphData();
        return ResponseEntity.ok(graph);
    }
}
