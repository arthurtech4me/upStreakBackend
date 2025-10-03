package com.upstreak.habits.controller;

import com.upstreak.habits.DTOs.AchievementDTO;
import com.upstreak.habits.DTOs.CategoryDTO;
import com.upstreak.habits.DTOs.UserAchievementDTO;
import com.upstreak.habits.model.UserAchievement;
import com.upstreak.habits.service.AchievementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {
    @Autowired
    private AchievementServiceImpl service;

    @GetMapping
    public List<AchievementDTO> getAllAchievements() {
        return service.findAll();
    }

    @GetMapping("/user/{id}")
    public List<UserAchievementDTO> getUserAchievements(@PathVariable Long id) {
        return service.getUserAchievements(id);
    }

    @PostMapping
    public ResponseEntity<List<AchievementDTO>> insertAchievements(@RequestBody List<AchievementDTO> achievments) {
        List<AchievementDTO> inserted = service.insertAchievements(achievments);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(inserted);
    }
}
