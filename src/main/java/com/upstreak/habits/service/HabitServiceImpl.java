package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.CategoryDTO;
import com.upstreak.habits.DTOs.HabitDTO;
import com.upstreak.habits.DTOs.UserDTO;
import com.upstreak.habits.model.*;
import com.upstreak.habits.model.enums.AchievementCode;
import com.upstreak.habits.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HabitServiceImpl implements HabitService {
    @Autowired
    private HabitRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private AchievementServiceImpl achievementService;

    @Autowired
    private StreakService streakService;

    @Override
    public List<HabitDTO> findAllByUsername(String username) {
        List<Habit> habits = repository.findHabitByUserUsername(username);
        return habits.stream().map(HabitDTO::from).toList();
    }

    @Override
    public Optional<HabitDTO> findById(Long id) {
        return repository.findById(id).map(HabitDTO::from);
    }

    @Override
    public HabitDTO createHabit(HabitDTO habit) {
        Habit newHabit = new Habit();
        newHabit.setName(habit.name());
        newHabit.setDescription(habit.description());
        Color color = colorRepository.findById(habit.color().id())
                .orElseThrow(() -> new IllegalArgumentException("Color not found!"));
        Set<Category> categories = habit.categories().stream()
                .map(c -> categoryRepository.findById(c.id())
                        .orElseThrow(() -> new IllegalArgumentException(("Category not found!"))))
                .collect(Collectors.toSet());
        newHabit.setColor(color);
        newHabit.setCategories(categories);
        User user = userRepository.findById(habit.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        newHabit.setUser(user);
        newHabit = repository.save(newHabit);
        achievementService.checkAndUnlockAllAchievements(user);
        return HabitDTO.from(newHabit);
    }

    @Override
    public HabitDTO updateById(Long id, HabitDTO habit) {
        Optional<Habit> habitFind = repository.findById(id);

        if (habitFind.isPresent()) {
            Habit habitToUpdate = habitFind.get();

            habitToUpdate.setName(habit.name());
            habitToUpdate.setName(habit.name());
            Color color = colorRepository.findById(habit.color().id())
                    .orElseThrow(() -> new IllegalArgumentException("Color not found!"));
            Set<Category> categories = habit.categories().stream()
                    .map(c -> categoryRepository.findById(c.id())
                            .orElseThrow(() -> new IllegalArgumentException(("Category not found!"))))
                    .collect(Collectors.toSet());
            habitToUpdate.setCategories(categories);
            habitToUpdate.setColor(color);
            User user = userRepository.findById(habit.userId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            habitToUpdate.setUser(user);
            habitToUpdate.setDescription(habit.description());

            Habit updatedHabit = repository.save(habitToUpdate);
            return HabitDTO.from(updatedHabit);

        } else {
            throw new RuntimeException("Habit not found!");
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public HabitDTO incrementStreak(Long id) {
        Optional<Habit> habitFind = repository.findById(id);

        if (habitFind.isPresent()) {
            Habit habitToUpdate = habitFind.get();

            habitToUpdate.incrementStreak();

            return HabitDTO.from(repository.save(habitToUpdate));
        } else {
            throw new RuntimeException("Habit not found!");
        }
    }

    @Override
    public HabitDTO decreaseStreak(Long id) {
        Optional<Habit> habitFind = repository.findById(id);

        if (habitFind.isPresent()) {
            Habit habitToUpdate = habitFind.get();

            habitToUpdate.decreaseStreak();

            return HabitDTO.from(repository.save(habitToUpdate));
        } else {
            throw new RuntimeException("Habit not found!");
        }
    }

    @Override
    public HabitDTO resetStreak(Long id) {
        Optional<Habit> habitFind = repository.findById(id);

        if (habitFind.isPresent()) {
            Habit habitToUpdate = habitFind.get();

            habitToUpdate.resetStreak();

            return HabitDTO.from(repository.save(habitToUpdate));
        } else {
            throw new RuntimeException("Habit not found!");
        }
    }

    @Override
    @Transactional
    public HabitDTO addCheckIn(Long id, CheckIn checkIn) {
        Habit habit = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Habit not found!"));

        boolean alreadyCheckedInToday = habit.getCheckIns().stream()
                .anyMatch(ci -> ci.getDate().equals(checkIn.getDate()));

        if (alreadyCheckedInToday) {
            throw new IllegalArgumentException("Check-in already exists for this date");
        }

        checkIn.setHabit(habit);
        CheckIn savedCheckIn = checkInRepository.save(checkIn);
        achievementService.unlockAchievement(habit.getUser(), AchievementCode.FIRST_CHECKIN);
        int totalCheckins = checkInRepository.countByHabitUser(habit.getUser());
        if (totalCheckins >= 50)
            achievementService.unlockAchievement(habit.getUser(), AchievementCode.TOTAL_CHECKINS_50);
        if (totalCheckins >= 100)
            achievementService.unlockAchievement(habit.getUser(), AchievementCode.TOTAL_CHECKINS_100);
        int currentStreak = streakService.calculateStreak(habit.getUser().getId());
        if (currentStreak >= 7)
            achievementService.unlockAchievement(habit.getUser(), AchievementCode.CHECKIN_7_DAYS_ROW);
        if (currentStreak >= 30)
            achievementService.unlockAchievement(habit.getUser(), AchievementCode.CHECKIN_30_DAYS_ROW);
        achievementService.checkAndUnlockAllAchievements(habit.getUser());
        return HabitDTO.from(habit);
    }

    @Override
    @Transactional
    public HabitDTO removeCheckInFromHabit(Long habitId, Long checkInId) {
        Habit habit = repository.findById(habitId).orElseThrow(() -> new EntityNotFoundException("Habit not found!"));
        CheckIn checkIn = checkInRepository.findById(checkInId).orElseThrow(() -> new EntityNotFoundException("CheckIn not found!"));

        if (!checkIn.getHabit().getId().equals(habit.getId())) {
            throw new IllegalArgumentException("CheckIn does not belong to this Habit.");
        }

        checkInRepository.delete(checkIn);
        habit.getCheckIns().removeIf(ci -> ci.getId().equals(checkInId));
        achievementService.checkAndUnlockAllAchievements(habit.getUser());
        return HabitDTO.from(repository.save(habit));
    }

}
