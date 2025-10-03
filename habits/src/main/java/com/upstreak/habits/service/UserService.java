package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.UserDTO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAll();
    Optional<UserDTO> findById(Long id);
    UserDTO createUser(UserDTO user);
    UserDTO updateById(Long id, UserDTO user);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    UserDTO findByUsername(String username);

    void setProfileImagePath(Long id, String imagePath);
}
