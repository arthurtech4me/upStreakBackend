package com.upstreak.habits.controller;

import com.upstreak.habits.DTOs.CategoryDTO;
import com.upstreak.habits.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl service;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = service.findAll();
        return ResponseEntity.ok().body(categories);
    }

    @PostMapping
    public ResponseEntity<List<CategoryDTO>> insertCategories(@RequestBody List<CategoryDTO> categories) {
        List<CategoryDTO> inserted = service.insertCategories(categories);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(inserted);
    }
}
