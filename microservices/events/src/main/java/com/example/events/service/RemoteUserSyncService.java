package com.example.events.service;

import com.example.events.client.UserClient;
import com.example.events.entity.User;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.external.Users;
import com.example.events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoteUserSyncService {

    private final UserClient userClient;
    private final UserRepository userRepository;

    public User fetchAndSaveUserIfMissing(UUID userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        Users remoteUser = userClient.getUserById(userId);

        if (remoteUser == null) {
            throw new ResourceNotFoundException("User not found in user-service with ID: " + userId);
        }

        User localUser = new User();
        localUser.setId(remoteUser.getId());
        localUser.setFirstName(remoteUser.getFirstName());
        localUser.setLastName(remoteUser.getLastName());
        localUser.setEmailAddress(remoteUser.getEmailAddress());
        localUser.setPicture(remoteUser.getPicture());
        localUser.setGender(remoteUser.getGender());
        localUser.setTShirtSize(remoteUser.getTShirtSize());
        localUser.setCountry(remoteUser.getCountry());

        try {
            return userRepository.save(localUser);
        } catch (Exception e) {
            System.out.println("Failed to save user: {}" + localUser);
            throw e;
        }
    }
}
