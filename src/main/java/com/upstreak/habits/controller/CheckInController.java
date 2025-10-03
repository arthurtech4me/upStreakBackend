package com.upstreak.habits.controller;

import com.upstreak.habits.DTOs.CheckInDTO;
import com.upstreak.habits.service.CheckInServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/all/checkins")
public class CheckInController {
    @Autowired
    private CheckInServiceImpl service;

    @GetMapping
    public ResponseEntity<List<CheckInDTO>> findAll() {
        List<CheckInDTO> checkIns = service.findAll();

        return ResponseEntity.ok().body(checkIns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CheckInDTO>> findAllByUser(@PathVariable Long id) {
        List<CheckInDTO> checkIns = service.findAllByUser(id);

        return ResponseEntity.ok().body(checkIns);
    }
}
