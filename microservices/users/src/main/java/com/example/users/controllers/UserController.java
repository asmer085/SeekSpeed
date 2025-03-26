package com.example.users.controllers;
import com.example.users.entity.Users;
import com.example.users.repository.UserRepository;
import com.example.users.services.UserService;
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
    public @ResponseBody Users getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/add")
    public @ResponseBody Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public @ResponseBody ResponseEntity<Users> updateUser(@PathVariable UUID userId, @RequestBody Users updatedUser) {
        return userService.updateUser(userId, updatedUser);
    }

    @DeleteMapping("/{userId}")
    public @ResponseBody ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {
        return userService.deleteUser(userId);
    }


}
