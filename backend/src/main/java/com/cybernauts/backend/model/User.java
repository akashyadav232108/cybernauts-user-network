package com.cybernauts.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Username is required")
    private String username;

    @Column(nullable = false)
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    private int age;

    @ElementCollection
    @CollectionTable(name = "user_hobbies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "hobby")
    @NotNull(message = "Hobbies are required")
    private Set<String> hobbies = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "friendships",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> friends = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();

    @Transient
    private double popularityScore;

    // -------------------- Helpers --------------------
    public void addFriend(User user) {
        friends.add(user);
        user.getFriends().add(this);
    }

    public void removeFriend(User user) {
        friends.remove(user);
        user.getFriends().remove(this);
    }


}
