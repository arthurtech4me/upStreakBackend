package com.upstreak.habits.DTOs;

import com.upstreak.habits.model.CheckIn;

public record CheckInDTO(Long id, String date, Long habitId) {
    public static CheckInDTO from(CheckIn checkIn) {
        return new CheckInDTO(checkIn.getId(), checkIn.getDate().toString(), checkIn.getHabit().getId());
    }
}
