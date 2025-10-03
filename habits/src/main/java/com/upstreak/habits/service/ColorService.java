package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.ColorDTO;
import com.upstreak.habits.model.Color;

import java.util.List;

public interface ColorService {
    List<ColorDTO> findAll();
    List<ColorDTO> insertColors(List<ColorDTO> colors);
}
