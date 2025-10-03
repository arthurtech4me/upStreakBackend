package com.upstreak.habits.service;

import com.upstreak.habits.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StreakService {
    @Autowired
    private CheckInRepository checkInRepository;

    public int calculateStreak(Long userId) {
        List<LocalDate> checkinDates = checkInRepository.findCheckInDatesByUserIdOrderByDateDesc(userId);

        if(checkinDates.isEmpty()) return 0;

        int streak = 1;
        LocalDate previous = checkinDates.get(0);

        for(int i = 1; i < checkinDates.size(); i++) {
            LocalDate current = checkinDates.get(i);

            if (previous.minusDays(1).equals(current)) {
                streak++;
                previous = current;
            } else if(previous.equals(current)) {
                continue;
            } else {
                break;
            }
        }

        return streak;
    }
}
