package com.upstreak.habits.repository;

import com.upstreak.habits.model.Achievement;
import com.upstreak.habits.model.User;
import com.upstreak.habits.model.UserAchievement;
import com.upstreak.habits.model.enums.AchievementCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    boolean existsByUserAndAchievement(User user, Achievement achievement);
    List<UserAchievement> findUserAchievementByUserId(Long userId);
}
