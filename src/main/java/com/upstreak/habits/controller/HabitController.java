package com.upstreak.habits.controller;

import com.upstreak.habits.DTOs.HabitDTO;
import com.upstreak.habits.DTOs.UserDTO;
import com.upstreak.habits.model.CheckIn;
import com.upstreak.habits.model.Habit;
import com.upstreak.habits.service.HabitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/habits")
public class HabitController {
    @Autowired
    private HabitServiceImpl service;

    @GetMapping
    public ResponseEntity<List<HabitDTO>> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<HabitDTO> habits = service.findAllByUsername(username);

        return ResponseEntity.ok().body(habits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitDTO> findById(@PathVariable Long id) {
        Optional<HabitDTO> habit = service.findById(id);
        if (habit.isPresent()) {
            return ResponseEntity.ok().body(habit.get());
        } else {
            throw new RuntimeException("Habit not found!");
        }
    }

    @PostMapping
    public ResponseEntity<HabitDTO> createHabit(@RequestBody HabitDTO habitDTO) {
        HabitDTO habit = service.createHabit(habitDTO);

        URI location = URI.create(String.format("/habits/%d", habit.id()));

        return ResponseEntity.created(location).body(habit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitDTO> updateHabit(@PathVariable Long id, @RequestBody HabitDTO habitDTO) {
        HabitDTO savedHabit = service.updateById(id, habitDTO);

        return ResponseEntity.ok().body(savedHabit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/incrementStreak")
    public ResponseEntity<HabitDTO> incrementStreak(@PathVariable Long id) {
        HabitDTO habit = service.incrementStreak(id);
        return ResponseEntity.ok().body(habit);
    }

    @PutMapping("/{id}/decreaseStreak")
    public ResponseEntity<HabitDTO> decreaseStreak(@PathVariable Long id) {
        HabitDTO habit = service.decreaseStreak(id);
        return ResponseEntity.ok().body(habit);
    }

    @PutMapping("/{id}/resetStreak")
    public ResponseEntity<HabitDTO> resetStreak(@PathVariable Long id) {
        HabitDTO habit = service.resetStreak(id);
        return ResponseEntity.ok().body(habit);
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<HabitDTO> addCheckin(@PathVariable Long id, @RequestBody CheckIn checkIn) {
        HabitDTO updatedHabit = service.addCheckIn(id, checkIn);
        return ResponseEntity.ok(updatedHabit);
    }

    @DeleteMapping("/{id}/checkin/remove/{checkInID}")
    public ResponseEntity<HabitDTO> removeCheckinFromHabit(@PathVariable Long id, @PathVariable Long checkInId) {
        HabitDTO updatedHabit = service.removeCheckInFromHabit(id, checkInId);
        return ResponseEntity.ok(updatedHabit);
    }
}
