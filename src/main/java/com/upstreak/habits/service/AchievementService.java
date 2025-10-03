package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.AchievementDTO;
import com.upstreak.habits.DTOs.CategoryDTO;
import com.upstreak.habits.DTOs.UserAchievementDTO;
import com.upstreak.habits.model.Achievement;
import com.upstreak.habits.model.User;
import com.upstreak.habits.model.enums.AchievementCode;

import java.util.List;

public interface AchievementService {
    List<AchievementDTO> findAll();
    List<UserAchievementDTO> getUserAchievements(Long userId);
    void unlockAchievement(User user, AchievementCode code);
    List<AchievementDTO> insertAchievements(List<AchievementDTO> achievements);

}
