package com.upstreak.habits.DTOs;

import com.upstreak.habits.model.Habit;
import com.upstreak.habits.model.User;

import java.util.List;

public record UserDTO(Long id, String name, String username, String password, String email, List<HabitDTO> habits, String profileImage) {
    public static UserDTO from(User user) {
        List<HabitDTO> habitsDTOs = user.getHabits().stream().map(HabitDTO::from).toList();
        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getPassword(), user.getEmail(), habitsDTOs, user.getProfileImage());
    }
}
