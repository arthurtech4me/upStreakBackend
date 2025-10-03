package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.CheckInDTO;
import com.upstreak.habits.model.CheckIn;
import com.upstreak.habits.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInServiceImpl implements CheckInService{
    @Autowired
    private CheckInRepository repository;

    @Override
    public List<CheckInDTO> findAll() {
        return repository.findAll().stream().map(CheckInDTO::from).toList();
    }

    @Override
    public List<CheckInDTO> findAllByUser(Long id) {
        return repository.findAllCheckInByHabitUserId(id).stream().map(CheckInDTO::from).toList();
    }
}
