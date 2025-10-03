package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.CheckInDTO;

import java.util.List;

public interface CheckInService {
    List<CheckInDTO> findAll();
    List<CheckInDTO> findAllByUser(Long id);
}
