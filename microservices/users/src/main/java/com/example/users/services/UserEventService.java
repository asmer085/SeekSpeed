package com.example.users.services;

import com.example.users.dto.UserEventDTO;
import com.example.users.entity.UserEvent;
import com.example.users.entity.Users;
import com.example.users.repository.UserEventRepository;
import com.example.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserEventService {

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserRepository userRepository;

    public Iterable<UserEvent> getAll() {
        return userEventRepository.findAll();
    }

    public UserEventDTO createUserEvent(UserEventDTO userEventDTO) {

        UUID userId = userEventDTO.getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserService.UserNotFoundException("User with id " + userId + " not found"));

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEventID(userEventDTO.getEventId());

        UserEvent savedEvent = userEventRepository.save(userEvent);

        UserEventDTO responseDTO = new UserEventDTO();
        responseDTO.setEventId(savedEvent.getEventID());
        responseDTO.setUserId(savedEvent.getUser().getId());

        return responseDTO;
    }

    public void saveUserEvent(UserEventDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEventID(dto.getEventId());

        userEventRepository.save(userEvent);
    }
}
