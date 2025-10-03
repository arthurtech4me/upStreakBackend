package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.AchievementDTO;
import com.upstreak.habits.DTOs.CategoryDTO;
import com.upstreak.habits.DTOs.UserAchievementDTO;
import com.upstreak.habits.model.Achievement;
import com.upstreak.habits.model.Category;
import com.upstreak.habits.model.User;
import com.upstreak.habits.model.UserAchievement;
import com.upstreak.habits.model.enums.AchievementCode;
import com.upstreak.habits.repository.AchievementRepository;
import com.upstreak.habits.repository.UserAchievementRepository;
import com.upstreak.habits.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService{
    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private StreakService streakService;

    @Override
    public List<AchievementDTO> findAll() {
        return achievementRepository.findAll().stream().map(AchievementDTO::from).toList();
    }

    @Override
    public List<UserAchievementDTO> getUserAchievements(Long userId) {
        List<Achievement> allAchievements = achievementRepository.findAll();

        List<UserAchievement> unlocked = userAchievementRepository.findUserAchievementByUserId(userId);
        Set<Long> unlockedIds = unlocked.stream()
                .map(ua -> ua.getAchievement().getId())
                .collect(Collectors.toSet());

        return allAchievements.stream()
                .map(achievement -> {
                    boolean isUnlocked = unlockedIds.contains(achievement.getId());
                    return UserAchievementDTO.from(achievement, isUnlocked);
                })
                .toList();
    }

    @Override
    public void unlockAchievement(User user, AchievementCode code) {
        Achievement achievement = achievementRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Achievement not found!"));

        if (userAchievementRepository.existsByUserAndAchievement(user, achievement)) return;

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setUnlockedAt(LocalDate.now());

        userAchievementRepository.save(userAchievement);

        System.out.println("Achievement unlocked! User: " + user.getUsername() + ", Achievement: " + achievement.getName());
    }

    @Override
    public List<AchievementDTO> insertAchievements(List<AchievementDTO> achievements) {
        return achievementRepository.saveAll(achievements.stream().map(Achievement::from).toList()).stream().map(AchievementDTO::from).toList();
    }

    public void checkAndUnlockAllAchievements(User user) {
        // FIRST_HABIT_CREATED
        if (user.getHabits() != null && !user.getHabits().isEmpty()) {
            unlockAchievement(user, AchievementCode.FIRST_HABIT_CREATED);
        }
        // CREATE_5_HABITS
        if (user.getHabits() != null && user.getHabits().size() >= 5) {
            unlockAchievement(user, AchievementCode.CREATE_5_HABITS);
        }
        // CREATE_10_HABITS
        if (user.getHabits() != null && user.getHabits().size() >= 10) {
            unlockAchievement(user, AchievementCode.CREATE_10_HABITS);
        }
        // CREATE_3_CATEGORIES
        // Supondo que cada hábito pode ter várias categorias, e queremos 3 categorias distintas
        var allCategories = user.getHabits().stream()
            .flatMap(h -> h.getCategories().stream())
            .map(Category::getId)
            .distinct()
            .toList();
        if (allCategories.size() >= 3) {
            unlockAchievement(user, AchievementCode.CREATE_3_CATEGORIES);
        }
        // FIRST_CHECKIN
        int totalCheckins = checkInRepository.countByHabitUser(user);
        if (totalCheckins >= 1) {
            unlockAchievement(user, AchievementCode.FIRST_CHECKIN);
        }
        // TOTAL_CHECKINS_50
        if (totalCheckins >= 50) {
            unlockAchievement(user, AchievementCode.TOTAL_CHECKINS_50);
        }
        // TOTAL_CHECKINS_100
        if (totalCheckins >= 100) {
            unlockAchievement(user, AchievementCode.TOTAL_CHECKINS_100);
        }
        // CHECKIN_7_DAYS_ROW e CHECKIN_30_DAYS_ROW
        int currentStreak = 0;
        try {
            currentStreak = streakService.calculateStreak(user.getId());
        } catch (Exception ignored) {}
        if (currentStreak >= 7) {
            unlockAchievement(user, AchievementCode.CHECKIN_7_DAYS_ROW);
        }
        if (currentStreak >= 30) {
            unlockAchievement(user, AchievementCode.CHECKIN_30_DAYS_ROW);
        }

        var checkinDates = checkInRepository.findCheckInDatesByUserIdOrderByDateDesc(user.getId());
        if (!checkinDates.isEmpty()) {
            var minDate = checkinDates.get(checkinDates.size() - 1);
            var maxDate = checkinDates.get(0);
            if (maxDate.minusDays(6).isAfter(minDate) || maxDate.minusDays(6).isEqual(minDate)) {
                unlockAchievement(user, AchievementCode.FIRST_WEEK_COMPLETE);
            }
            if (maxDate.minusMonths(1).isAfter(minDate) || maxDate.minusMonths(1).isEqual(minDate)) {
                unlockAchievement(user, AchievementCode.FIRST_MONTH_COMPLETE);
            }
            if (maxDate.minusMonths(3).isAfter(minDate) || maxDate.minusMonths(3).isEqual(minDate)) {
                unlockAchievement(user, AchievementCode.FIRST_3_MONTHS);
            }
            if (maxDate.minusYears(1).isAfter(minDate) || maxDate.minusYears(1).isEqual(minDate)) {
                unlockAchievement(user, AchievementCode.FIRST_YEAR);
            }
        }
        // HEALTHY_HABITS_5
        long healthyHabits = user.getHabits().stream()
            .filter(h -> h.getCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase("Saude")))
            .count();
        if (healthyHabits >= 5) {
            unlockAchievement(user, AchievementCode.HEALTHY_HABITS_5);
        }
        // MINDFULNESS_10
        long mindfulnessHabits = user.getHabits().stream()
            .filter(h -> h.getCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase("Mindfulness")))
            .count();
        if (mindfulnessHabits >= 10) {
            unlockAchievement(user, AchievementCode.MINDFULNESS_10);
        }
        // SLEEP_HABIT_COMPLETE_30
        boolean sleepHabit30 = user.getHabits().stream()
            .filter(h -> h.getCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase("Sono")))
            .anyMatch(h -> h.getCheckIns().size() >= 30);
        if (sleepHabit30) {
            unlockAchievement(user, AchievementCode.SLEEP_HABIT_COMPLETE_30);
        }
        // READING_10_DAYS
        boolean reading10 = user.getHabits().stream()
            .filter(h -> h.getCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase("Leitura")))
            .anyMatch(h -> h.getCheckIns().size() >= 10);
        if (reading10) {
            unlockAchievement(user, AchievementCode.READING_10_DAYS);
        }
        // NO_SKIP_WEEK: fez check-in em todos os hábitos por 7 dias seguidos
        if (!user.getHabits().isEmpty()) {
            boolean noSkipWeek = true;
            for (int i = 0; i < 7; i++) {
                LocalDate day = LocalDate.now().minusDays(i);
                for (var habit : user.getHabits()) {
                    boolean checked = habit.getCheckIns().stream().anyMatch(ci -> ci.getDate().equals(day));
                    if (!checked) {
                        noSkipWeek = false;
                        break;
                    }
                }
                if (!noSkipWeek) break;
            }
            if (noSkipWeek) {
                unlockAchievement(user, AchievementCode.NO_SKIP_WEEK);
            }
        }
        // TRY_NEW_HABIT: criou e concluiu o primeiro hábito
        boolean tryNewHabit = !user.getHabits().isEmpty() && user.getHabits().stream().anyMatch(h -> !h.getCheckIns().isEmpty());
        if (tryNewHabit) {
            unlockAchievement(user, AchievementCode.TRY_NEW_HABIT);
        }
    }
}
