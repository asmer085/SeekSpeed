package com.example.users.controllers;

import com.example.users.dto.UserDTO;
import com.example.users.entity.Users;
import com.example.users.services.UserService;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/users")
@Validated
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

    @GetMapping("/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        try {
            Users user = userService.getUserByUsername(username);
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

    @PostMapping("/batch")
    public @ResponseBody ResponseEntity<?> createUsersBatch(@Valid @RequestBody List<UserDTO> usersDTO) {
        try {
            List<UserDTO> createdUsers = userService.createUsersBatch(usersDTO);
            return ResponseEntity.ok(createdUsers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> patchUpdateUser(@PathVariable UUID userId, @RequestBody JsonPatch patch) {
        try {
            Users updatedUser = userService.applyPatchToUser(patch, userId);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public @ResponseBody ResponseEntity<Users> updateUser(@PathVariable UUID userId,@Valid @RequestBody UserDTO updatedUserDTO) {
        return userService.updateUser(userId, updatedUserDTO);
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
