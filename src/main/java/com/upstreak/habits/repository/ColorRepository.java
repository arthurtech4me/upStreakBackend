package com.upstreak.habits.repository;

import com.upstreak.habits.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
}
