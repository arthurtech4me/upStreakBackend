package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();

    List<CategoryDTO> insertCategories(List<CategoryDTO> categories);
}
