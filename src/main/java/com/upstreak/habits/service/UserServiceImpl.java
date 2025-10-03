package com.upstreak.habits.service;

import com.upstreak.habits.DTOs.UserDTO;
import com.upstreak.habits.exceptions.UserNotFoundException;
import com.upstreak.habits.model.User;
import com.upstreak.habits.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAll() {
        List<User> users = repository.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        users.forEach(u -> {
            UserDTO userDto = UserDTO.from(u);
            usersDTO.add(userDto);
        });
        return usersDTO;
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        return repository.findById(id).map(UserDTO::from);
    }

    @Override
    public UserDTO createUser(UserDTO user) {
        if (repository.findUserByUsername(user.username()).isPresent()) {
            throw new IllegalArgumentException("Username already in use");
        }

        if(repository.findUserByEmail(user.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User newUser = new User();
        newUser.setName(user.name());
        newUser.setEmail(user.email());
        newUser.setUsername(user.username());
        newUser.setPassword(passwordEncoder.encode(user.password()));
        User createdUser = repository.save(newUser);
        return UserDTO.from(createdUser);
    }

    @Override
    public UserDTO updateById(Long id, UserDTO user) {
        Optional<User> userFind = repository.findById(id);

        if (userFind.isPresent()) {
            User userToUpdate = userFind.get();

            userToUpdate.setName(user.name());
            userToUpdate.setUsername(user.username());
            userToUpdate.setPassword(user.password());
            userToUpdate.setEmail(user.email());

            User updatedUser = repository.save(userToUpdate);
            return UserDTO.from(updatedUser);

        } else {
            throw new RuntimeException("User not found!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return UserDTO.from(user);
    }

    @Override
    public void setProfileImagePath(Long id, String imagePath) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfileImage(imagePath);
            repository.save(user);
        } else {
            throw new UserNotFoundException("User not found!");
        }
    }


}
