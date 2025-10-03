package com.upstreak.habits.DTOs;

import com.upstreak.habits.model.Achievement;
import com.upstreak.habits.model.enums.AchievementCode;

public record AchievementDTO(Long id, AchievementCode code, String name, String description) {
    public static AchievementDTO from(Achievement achievement) {
        return new AchievementDTO(achievement.getId(), achievement.getCode(), achievement.getName(), achievement.getDescription());
    }
}
