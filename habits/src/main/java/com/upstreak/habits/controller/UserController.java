package com.upstreak.habits.controller;

import com.upstreak.habits.DTOs.UserDTO;
import com.upstreak.habits.exceptions.UserNotFoundException;
import com.upstreak.habits.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServiceImpl service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = service.findAll();

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        Optional<UserDTO> user = service.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        } else {
            throw new UserNotFoundException("User not found!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<UserDTO> user = service.findById(id);
        if (user.isPresent()) {
            UserDTO savedUser = service.updateById(id, userDTO);
            return ResponseEntity.ok().body(savedUser);
        } else {
            throw new UserNotFoundException("User not found!");
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<UserDTO> optionalUser = service.findById(id);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String nomeArquivo = UUID.randomUUID() + "-" + file.getOriginalFilename();
            String diretorio = "/tmp/uploads/";
            String caminhoCompleto = diretorio + nomeArquivo;

            File diretorioUpload = new File(diretorio);
            if (!diretorioUpload.exists()) {
                diretorioUpload.mkdirs();
            }

            file.transferTo(new File(caminhoCompleto));

            service.setProfileImagePath(id, caminhoCompleto);

            return ResponseEntity.ok("Imagem enviada com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao salvar imagem: " + e.getMessage());
        }
    }
}
