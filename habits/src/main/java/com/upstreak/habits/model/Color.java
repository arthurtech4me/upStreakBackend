package com.upstreak.habits.model;

import com.upstreak.habits.DTOs.ColorDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "color")
    private Set<Habit> habits = new HashSet<>();

    public static Color from (ColorDTO color) {
        return new Color(color.name());
    }

    public Color() {
    }

    public Color(String name) {
        this.name = name;
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

    public Set<Habit> getHabits() {
        return habits;
    }
}
