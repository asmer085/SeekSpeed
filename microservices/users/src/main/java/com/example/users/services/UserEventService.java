package com.example.users.services;

import com.example.users.dtos.UserEventDTO;
import com.example.users.entity.UserEvent;
import com.example.users.entity.Users;
import com.example.users.repository.UserEventRepository;
import com.example.users.repository.UserRepository;
import com.example.users.services.UserService;
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

        UUID userId = userEventDTO.getUser().getId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserService.UserNotFoundException("User with id " + userId + " not found"));

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEventID(userEventDTO.getEventID());

        UserEvent savedEvent = userEventRepository.save(userEvent);

        UserEventDTO responseDTO = new UserEventDTO();
        responseDTO.setEventID(savedEvent.getEventID());
        responseDTO.setUser(savedEvent.getUser());

        return responseDTO;
    }
}
