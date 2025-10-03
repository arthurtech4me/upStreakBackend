package com.upstreak.habits.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upstreak.habits.DTOs.HabitDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_habits")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
    private Integer streak = 0;
    private Integer maxStreak = 0;

    @ManyToMany
    @JoinTable(
            name = "habit_category",
            joinColumns = @JoinColumn(name = "habit_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CheckIn> checkIns = new ArrayList<>();

    public Habit() {
    }

    public Habit(Long id, String name, String description, Set<Category> categories, Color color, User user, Integer streak, Integer maxStreak) {
        this.id = id;
        this.name = name;
        this.description = description;
        categories.forEach(c -> getCategories().add(c));
        this.color = color;
        this.streak = streak;
        this.maxStreak = maxStreak;
        this.user = user;
    }

    public void incrementStreak() {
        this.streak += 1;
        if (this.streak > this.maxStreak) {
            this.maxStreak = this.streak;
        }
    }

    public void resetStreak() {
        this.streak = 0;
    }

    public void decreaseStreak() {
        if (this.streak > 0) {
            this.streak -= 1;
            this.maxStreak -= 1;
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getStreak() {
        return streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }

    public Integer getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(Integer maxStreak) {
        this.maxStreak = maxStreak;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<CheckIn> getCheckIns() {
        return checkIns;
    }
}
