package com.upstreak.habits.controller;

import com.upstreak.habits.DTOs.AuthResponse;
import com.upstreak.habits.DTOs.LoginDTO;
import com.upstreak.habits.DTOs.UserDTO;
import com.upstreak.habits.security.JwtUtil;
import com.upstreak.habits.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody UserDTO request) throws RuntimeException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        String token = jwtUtil.generateToken(request.username());
        UserDTO user = userService.findByUsername(request.username());
        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody UserDTO request) {
        UserDTO user = userService.createUser(request);
        URI location = URI.create(String.format("/users/%d", user.id()));
        return ResponseEntity.created(location).body(user);
    }
}
