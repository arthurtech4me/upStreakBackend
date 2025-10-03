package com.upstreak.habits.repository;

import com.upstreak.habits.model.Achievement;
import com.upstreak.habits.model.enums.AchievementCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByCode(AchievementCode code);
}
