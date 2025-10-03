package com.upstreak.habits.DTOs;

import com.upstreak.habits.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record HabitDTO(
        Long id,
        String name,
        String description,
        ColorDTO color,
        Set<CategoryDTO> categories,
        Long userId,
        Integer streak,
        Integer maxStreak,
        List<CheckInDTO> checkIns
) {
    public static HabitDTO from(Habit habit) {
        return new HabitDTO(
                habit.getId(),
                habit.getName(),
                habit.getDescription(),
                habit.getColor() != null ? ColorDTO.from(habit.getColor()) : null,
                habit.getCategories().stream().map(CategoryDTO::from).collect(Collectors.toSet()),
                habit.getUser() != null ? habit.getUser().getId() : null,
                habit.getStreak(),
                habit.getMaxStreak(),
                habit.getCheckIns().stream().map(CheckInDTO::from).toList()
        );
    }
}
