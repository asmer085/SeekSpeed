package com.example.users.controllers;

import com.example.users.dtos.UserDTO;
import com.example.users.entity.Users;
import com.example.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path="/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public @ResponseBody Iterable<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable UUID userId) {
        try {
            Users user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (UserService.UserNotFoundException e) {
                return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public @ResponseBody ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{userId}")
    public @ResponseBody ResponseEntity<Users> updateUser(@PathVariable UUID userId, @Valid @RequestBody Users updatedUser) {
        return userService.updateUser(userId, updatedUser);
    }

    @DeleteMapping("/{userId}")
    public @ResponseBody ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {
        return userService.deleteUser(userId);
    }

    @ExceptionHandler(UserService.UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserService.UserNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}


