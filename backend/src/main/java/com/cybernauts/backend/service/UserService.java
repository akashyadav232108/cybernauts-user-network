package com.cybernauts.backend.service;

import com.cybernauts.backend.dto.UserDTO;
import com.cybernauts.backend.exception.RelationshipConflictException;
import com.cybernauts.backend.exception.UserNotFoundException;
import com.cybernauts.backend.model.User;
import com.cybernauts.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // -------------------- CRUD --------------------
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.debug("Retrieved {} users", users.size());
        return users;
    }

    public User getUserById(UUID id) {
        if (id == null) {
            log.error("getUserById called with null ID");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found");
                });
        log.debug("User found: {}", user.getUsername());
        return user;
    }

    public User createUser(User user) {
        if (user == null) {
            log.error("createUser called with null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            log.error("createUser called with null or empty username");
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        log.info("Creating new user: {}", user.getUsername());
        
        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("Attempted to create user with duplicate username: {}", user.getUsername());
            throw new RelationshipConflictException("Username already exists");
        }

        if (user.getHobbies() == null) {
            user.setHobbies(new HashSet<>());
        }

        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getId());
        return savedUser;
    }




    public User updateUser(UUID id, User updatedUser) {
        if (id == null) {
            log.error("updateUser called with null ID");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (updatedUser == null) {
            log.error("updateUser called with null updatedUser");
            throw new IllegalArgumentException("Updated user data cannot be null");
        }
        if (updatedUser.getUsername() == null || updatedUser.getUsername().trim().isEmpty()) {
            log.error("updateUser called with null or empty username");
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found");
                });

        user.setUsername(updatedUser.getUsername());
        user.setAge(updatedUser.getAge());
        user.setHobbies(updatedUser.getHobbies() != null ? updatedUser.getHobbies() : new HashSet<>());

        User savedUser = userRepository.save(user);
        log.info("Successfully updated user with ID: {}", id);
        return savedUser;
    }


    public void deleteUser(UUID id) {
        if (id == null) {
            log.error("deleteUser called with null ID");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        log.info("Attempting to delete user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found");
                });

        if (user.getFriends() != null && !user.getFriends().isEmpty()) {
            log.warn("Cannot delete user {} - still has {} friend(s)", id, user.getFriends().size());
            throw new RelationshipConflictException("Unlink user from friends before deletion");
        }

        userRepository.delete(user);
        log.info("Successfully deleted user with ID: {}", id);
    }

    // -------------------- FRIENDSHIP --------------------
    public void linkUsers(UUID userId, UUID friendId) {
        if (userId == null) {
            log.error("linkUsers called with null userId");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (friendId == null) {
            log.error("linkUsers called with null friendId");
            throw new IllegalArgumentException("Friend ID cannot be null");
        }
        
        log.info("Linking users: {} and {}", userId, friendId);
        
        if (userId.equals(friendId)) {
            log.warn("Attempted to link user to self: {}", userId);
            throw new RelationshipConflictException("Cannot link user to self");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found");
                });
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> {
                    log.error("Friend not found with ID: {}", friendId);
                    return new UserNotFoundException("Friend not found");
                });

        if (user.getFriends().contains(friend)) {
            log.warn("Users {} and {} are already friends", userId, friendId);
            throw new RelationshipConflictException("Users are already friends");
        }

        user.addFriend(friend);

        userRepository.save(user);
        userRepository.save(friend);
        log.info("Successfully linked users: {} and {}", userId, friendId);
    }

    public void unlinkUsers(UUID userId, UUID friendId) {
        if (userId == null) {
            log.error("unlinkUsers called with null userId");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (friendId == null) {
            log.error("unlinkUsers called with null friendId");
            throw new IllegalArgumentException("Friend ID cannot be null");
        }
        
        log.info("Unlinking users: {} and {}", userId, friendId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found");
                });
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> {
                    log.error("Friend not found with ID: {}", friendId);
                    return new UserNotFoundException("Friend not found");
                });

        if (!user.getFriends().contains(friend)) {
            log.warn("Users {} and {} are not friends", userId, friendId);
            throw new RelationshipConflictException("Users are not friends");
        }

        user.removeFriend(friend);

        userRepository.save(user);
        userRepository.save(friend);
        log.info("Successfully unlinked users: {} and {}", userId, friendId);
    }

    // -------------------- POPULARITY SCORE --------------------
    public double computePopularityScore(User user) {
        if (user == null) {
            log.error("computePopularityScore called with null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (user.getHobbies() == null) {
            user.setHobbies(new HashSet<>());
        }
        
        int uniqueFriends = user.getFriends().size();

        long sharedHobbies = user.getFriends().stream()
                .mapToLong(friend -> {
                    if (friend == null || friend.getHobbies() == null || user.getHobbies() == null) {
                        return 0;
                    }
                    Set<String> shared = new HashSet<>(friend.getHobbies());
                    shared.retainAll(user.getHobbies());
                    return shared.size();
                })
                .sum();

        double score = uniqueFriends + (sharedHobbies * 0.5);
        log.debug("Computed popularity score for user {}: {} (friends: {}, shared hobbies: {})", 
                     user.getId(), score, uniqueFriends, sharedHobbies);
        return score;
    }


    // -------------------- GRAPH DATA --------------------
    public Map<String, Object> getGraphData() {
        log.info("Generating graph data");
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
        
        log.info("Graph data generated with {} nodes and {} edges", nodes.size(), edges.size());
        return graph;
    }


    // -------------------- Mapping User → UserDTO --------------------
    public UserDTO toDTO(User user) {
        if (user == null) {
            log.error("toDTO called with null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        
        Set<UUID> friendIds = (user.getFriends() != null) 
                ? user.getFriends().stream()
                    .filter(friend -> friend != null && friend.getId() != null)
                    .map(User::getId)
                    .collect(Collectors.toSet())
                : new HashSet<>();

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
