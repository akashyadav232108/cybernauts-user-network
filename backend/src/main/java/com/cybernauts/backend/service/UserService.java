package com.cybernauts.backend.service;

import com.cybernauts.backend.model.User;
import com.cybernauts.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Create new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Update user
    public User updateUser(UUID id, User updatedUser) {
        // Logic to update user
        return null; // placeholder
    }

    // Delete user
    public void deleteUser(UUID id) {
        // Logic to check unlink before delete
    }

    // Link friendship
    public void linkUsers(UUID userId, UUID friendId) {
        // Logic to add friend, prevent circular duplicate
    }

    // Unlink friendship
    public void unlinkUsers(UUID userId, UUID friendId) {
        // Logic to remove friendship
    }

    // Compute popularity score
    public double computePopularityScore(User user) {
        // Implement formula: unique friends + (shared hobbies × 0.5)
        return 0.0; // placeholder
    }
}

