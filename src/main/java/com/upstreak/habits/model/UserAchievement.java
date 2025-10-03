package com.upstreak.habits.model;

import com.upstreak.habits.DTOs.UserAchievementDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_user_achievements")
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Achievement achievement;

    private LocalDate unlockedAt;

    public UserAchievement(){
    }

    public UserAchievement(Long id, User user, Achievement achievement, LocalDate unlockedAt) {
        this.id = id;
        this.user = user;
        this.achievement = achievement;
        this.unlockedAt = unlockedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public LocalDate getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(LocalDate unlockedAt) {
        this.unlockedAt = unlockedAt;
    }
}
