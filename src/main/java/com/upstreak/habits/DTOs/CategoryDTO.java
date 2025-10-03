package com.upstreak.habits.DTOs;

import com.upstreak.habits.model.Category;

public record CategoryDTO(Long id, String name) {
    public static CategoryDTO from(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
