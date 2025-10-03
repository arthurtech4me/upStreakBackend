package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.HabitDTO;
import com.upstreak.habits.model.CheckIn;

import java.util.List;
import java.util.Optional;

public interface HabitService {
    List<HabitDTO> findAllByUsername(String username);

    Optional<HabitDTO> findById(Long id);

    HabitDTO createHabit(HabitDTO user);

    HabitDTO updateById(Long id, HabitDTO user);

    void deleteById(Long id);

    HabitDTO incrementStreak(Long id);
    HabitDTO decreaseStreak(Long id);
    HabitDTO resetStreak(Long id);
    HabitDTO addCheckIn(Long id, CheckIn checkIn);
    HabitDTO removeCheckInFromHabit(Long habitId, Long checkInId);
}
