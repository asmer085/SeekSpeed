package com.example.users.controllers;
import com.example.users.entity.Users;
import com.example.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path="/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Users> getAllUsers() {return userRepository.findAll();}

    @GetMapping("/{userId}")
    public @ResponseBody Users getUserById(@PathVariable UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
    }

    @PostMapping("/add")
    public @ResponseBody Users createUser(@RequestBody Users user) {return userRepository.save(user);}

    @PutMapping("/{userId}")
    public @ResponseBody ResponseEntity<Users> updateUser(@PathVariable UUID userId, @RequestBody Users updatedUser) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    if (updatedUser.getFirstName() != null) existingUser.setFirstName(updatedUser.getFirstName());
                    if (updatedUser.getLastName() != null) existingUser.setLastName(updatedUser.getLastName());
                    if (updatedUser.getEmailAddress() != null) existingUser.setEmailAddress(updatedUser.getEmailAddress());
                    if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
                    if (updatedUser.getRole() != null) existingUser.setRole(updatedUser.getRole());
                    if (updatedUser.getPicture() != null) existingUser.setPicture(updatedUser.getPicture());
                    if (updatedUser.getDateOfBirth() != null) existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
                    if (updatedUser.getGender() != null) existingUser.setGender(updatedUser.getGender());
                    if (updatedUser.getTShirtSize() != null) existingUser.settShirtSize(updatedUser.getTShirtSize());
                    if (updatedUser.getOrganisationFile() != null) existingUser.setOrganisationFile(updatedUser.getOrganisationFile());
                    if (updatedUser.getCountry() != null) existingUser.setCountry(updatedUser.getCountry());

                    userRepository.save(existingUser);

                    return ResponseEntity.ok(existingUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{userId}")
    public @ResponseBody ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
