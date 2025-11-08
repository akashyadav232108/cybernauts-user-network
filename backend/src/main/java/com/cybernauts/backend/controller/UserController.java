package com.cybernauts.backend.controller;

import com.cybernauts.backend.model.User;
import com.cybernauts.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // POST /api/users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    // POST /api/users/{id}/link
    @PostMapping("/{id}/link")
    public void linkUser(@PathVariable UUID id, @RequestParam UUID friendId) {
        userService.linkUsers(id, friendId);
    }

    // DELETE /api/users/{id}/unlink
    @DeleteMapping("/{id}/unlink")
    public void unlinkUser(@PathVariable UUID id, @RequestParam UUID friendId) {
        userService.unlinkUsers(id, friendId);
    }
}

