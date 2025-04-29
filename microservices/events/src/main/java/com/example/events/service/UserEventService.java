package com.example.events.service;

import com.example.events.entity.User;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.UserEventRepository;
import com.example.events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserEventService {

    @Autowired
    private final UserEventRepository userEventRepository;
    @Autowired
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UUID> getEventIdsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return userEventRepository.findByUser(user)
                .stream()
                .map(userEvent -> userEvent.getEvent().getId())
                .toList();
    }
}

