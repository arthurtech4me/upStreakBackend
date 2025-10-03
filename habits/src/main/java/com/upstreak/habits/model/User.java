package com.upstreak.habits.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.upstreak.habits.DTOs.UserDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;

    @Column(name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "user")
    private List<UserAchievement> achievements;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private final List<Habit> habits = new ArrayList<>();

    // Várias conquistas

    public static User from(UserDTO userDTO) {
        return new User(userDTO.id(), userDTO.name(), userDTO.username(), userDTO.password(), userDTO.email());
    }

    public User() {
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public User(Long id, String name, String username, String password, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
