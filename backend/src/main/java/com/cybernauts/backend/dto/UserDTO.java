package com.cybernauts.backend.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO {

    private UUID id;
    private String username;
    private int age;
    private Set<String> hobbies;
    private double popularityScore;
    private Set<UUID> friendIds; // only IDs, no nested User objects

    // Constructor
    public UserDTO(UUID id, String username, int age, Set<String> hobbies,
                   double popularityScore, Set<UUID> friendIds) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.hobbies = hobbies;
        this.popularityScore = popularityScore;
        this.friendIds = friendIds;
    }

}
