package com.upstreak.habits.repository;

import com.upstreak.habits.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findHabitByUserUsername(String username);
}
