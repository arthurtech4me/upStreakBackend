package com.upstreak.habits.repository;

import com.upstreak.habits.DTOs.CheckInDTO;
import com.upstreak.habits.model.CheckIn;
import com.upstreak.habits.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    int countByHabitUser(User user);

    @Query("SELECT DISTINCT c.date FROM CheckIn c WHERE c.habit.user.id = :userId ORDER BY c.date DESC")
    List<LocalDate> findCheckInDatesByUserIdOrderByDateDesc(@Param("userId") Long userId);

    List<CheckIn> findAllCheckInByHabitUserId(Long id);
}
