package com.example.users.services;

import com.example.users.entity.Users;
import com.example.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public ResponseEntity<Users> updateUser(UUID userId, Users updatedUser) {
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

                    return ResponseEntity.ok(userRepository.save(existingUser));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteUser(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
