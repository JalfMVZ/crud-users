package com.example.demo.controller;

import com.example.demo.model.AppUser;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<AppUser> users = userRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Optional<AppUser> user = userRepository.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (user.isPresent()) {
            response.put("status", "success");
            response.put("data", user.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Usuario no encontrado con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody @Validated AppUser user, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        
        if (result.hasErrors()) {
            response.put("status", "error");
            response.put("message", "Datos de usuario inválidos");
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }

        if (!isValidEmail(user.getEmail())) {
            response.put("status", "error");
            response.put("message", "El formato del correo electrónico es inválido");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.findByName(user.getName()).isPresent()) {
            response.put("status", "error");
            response.put("message", "Ya existe un usuario con el nombre: " + user.getName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("status", "error");
            response.put("message", "Ya existe un usuario con el correo electrónico: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            AppUser createdUser = userRepository.save(user);
            response.put("status", "success");
            response.put("message", "Usuario creado exitosamente");
            response.put("data", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al crear el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody @Validated AppUser user,
            BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        
        if (result.hasErrors()) {
            response.put("status", "error");
            response.put("message", "Datos de usuario inválidos");
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }

        if (!isValidEmail(user.getEmail())) {
            response.put("status", "error");
            response.put("message", "El formato del correo electrónico es inválido");
            return ResponseEntity.badRequest().body(response);
        }

        if (!userRepository.existsById(id)) {
            response.put("status", "error");
            response.put("message", "Usuario no encontrado con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            user.setId(id);
            AppUser updatedUser = userRepository.save(user);
            response.put("status", "success");
            response.put("message", "Usuario actualizado exitosamente");
            response.put("data", updatedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al actualizar el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!userRepository.existsById(id)) {
            response.put("status", "error");
            response.put("message", "Usuario no encontrado con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            userRepository.deleteById(id);
            response.put("status", "success");
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al eliminar el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}