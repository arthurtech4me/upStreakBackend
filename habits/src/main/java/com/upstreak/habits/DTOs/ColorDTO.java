package com.upstreak.habits.DTOs;

import com.upstreak.habits.model.Color;

public record ColorDTO(Long id, String name) {
    public static ColorDTO from(Color color) {
        return new ColorDTO(color.getId(), color.getName());
    }
}
