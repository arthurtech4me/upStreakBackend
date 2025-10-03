package com.upstreak.habits.DTOs;

import com.upstreak.habits.model.Achievement;
import com.upstreak.habits.model.UserAchievement;

import java.time.LocalDate;

public record UserAchievementDTO(Long id, String title, String description, boolean unlocked) {
    public static UserAchievementDTO from(Achievement achievement, boolean unlocked) {
        return new UserAchievementDTO(achievement.getId(), achievement.getName(), achievement.getDescription(), unlocked);
    }
}
