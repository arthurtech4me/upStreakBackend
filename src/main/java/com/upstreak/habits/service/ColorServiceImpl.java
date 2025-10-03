package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.CategoryDTO;
import com.upstreak.habits.DTOs.ColorDTO;
import com.upstreak.habits.model.Category;
import com.upstreak.habits.model.Color;
import com.upstreak.habits.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository repository;

    @Override
    public List<ColorDTO> findAll() {
        return repository.findAll().stream().map(c -> new ColorDTO(c.getId(), c.getName())).toList();
    }

    @Override
    public List<ColorDTO> insertColors(List<ColorDTO> colors) {
        return repository.saveAll(colors.stream().map(Color::from).toList()).stream().map(ColorDTO::from).toList();
    }
}
