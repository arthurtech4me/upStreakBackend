package com.upstreak.habits.controller;

import com.upstreak.habits.DTOs.CategoryDTO;
import com.upstreak.habits.DTOs.ColorDTO;
import com.upstreak.habits.model.Color;
import com.upstreak.habits.service.ColorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/colors")
public class ColorController {
    @Autowired
    private ColorServiceImpl service;

    @GetMapping
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        List<ColorDTO> colors = service.findAll();
        return ResponseEntity.ok().body(colors);
    }

    @PostMapping
    public ResponseEntity<List<ColorDTO>> insertCategories(@RequestBody List<ColorDTO> colors) {
        List<ColorDTO> inserted = service.insertColors(colors);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(inserted);
    }
}
