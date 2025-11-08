package com.cybernauts.backend.service;

import com.cybernauts.backend.dto.UserDTO;
import com.cybernauts.backend.exception.RelationshipConflictException;
import com.cybernauts.backend.exception.UserNotFoundException;
import com.cybernauts.backend.model.User;
import com.cybernauts.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // -------------------- CRUD --------------------
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RelationshipConflictException("Username already exists");
        }

        if (user.getHobbies() == null) {
            user.setHobbies(new HashSet<>());
        }

        return userRepository.save(user);
    }




    public User updateUser(UUID id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUsername(updatedUser.getUsername());
        user.setAge(updatedUser.getAge());
        user.setHobbies(updatedUser.getHobbies() != null ? updatedUser.getHobbies() : new HashSet<>());

        return userRepository.save(user);
    }


    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getFriends().isEmpty()) {
            throw new RelationshipConflictException("Unlink user from friends before deletion");
        }

        userRepository.delete(user);
    }

    // -------------------- FRIENDSHIP --------------------
    public void linkUsers(UUID userId, UUID friendId) {
        if (userId.equals(friendId)) {
            throw new RelationshipConflictException("Cannot link user to self");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Friend not found"));

        if (user.getFriends().contains(friend)) {
            throw new RelationshipConflictException("Users are already friends");
        }

        user.addFriend(friend);

        userRepository.save(user);
        userRepository.save(friend);
    }

    public void unlinkUsers(UUID userId, UUID friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Friend not found"));

        if (!user.getFriends().contains(friend)) {
            throw new RelationshipConflictException("Users are not friends");
        }

        user.removeFriend(friend);

        userRepository.save(user);
        userRepository.save(friend);
    }

    // -------------------- POPULARITY SCORE --------------------
    public double computePopularityScore(User user) {
        int uniqueFriends = user.getFriends().size();

        long sharedHobbies = user.getFriends().stream()
                .mapToLong(friend -> {
                    Set<String> shared = new HashSet<>(friend.getHobbies());
                    shared.retainAll(user.getHobbies());
                    return shared.size();
                })
                .sum();

        return uniqueFriends + (sharedHobbies * 0.5);
    }


    // -------------------- GRAPH DATA --------------------
    public Map<String, Object> getGraphData() {
        List<User> users = userRepository.findAll();

        // Nodes
        List<Map<String, Object>> nodes = users.stream().map(u -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", u.getId().toString());
            node.put("username", u.getUsername());
            node.put("age", u.getAge());
            node.put("popularityScore", computePopularityScore(u));
            return node;
        }).collect(Collectors.toList());

        // Edges
        List<Map<String, String>> edges = new ArrayList<>();
        for (User u : users) {
            for (User f : u.getFriends()) {
                Map<String, String> edge = new HashMap<>();
                edge.put("source", u.getId().toString());
                edge.put("target", f.getId().toString());
                edges.add(edge);
            }
        }

        Map<String, Object> graph = new HashMap<>();
        graph.put("nodes", nodes);
        graph.put("edges", edges);

        return graph;
    }


    // -------------------- Mapping User → UserDTO --------------------
    public UserDTO toDTO(User user) {
        Set<UUID> friendIds = user.getFriends().stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        double popularityScore = computePopularityScore(user); // recompute every time

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getAge(),
                user.getHobbies(), // use getHobbies() now
                popularityScore,
                friendIds
        );
    }

}
