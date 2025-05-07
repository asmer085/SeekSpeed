package com.example.events.service;

import com.example.events.entity.Event;
import com.example.events.entity.User;
import com.example.events.entity.UserEvent;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.UserEventRepository;
import com.example.events.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEventServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventRepository userEventRepository;

    @InjectMocks
    private UserEventService userEventService;

    private UUID userId;
    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);

        event = new Event();
        event.setId(UUID.randomUUID());
    }

    @Test
    void getEventIdsByUserId_ValidUser_ShouldReturnEventIds() {
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEvent(event);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userEventRepository.findByUser(user)).thenReturn(List.of(userEvent));

        List<UUID> result = userEventService.getEventIdsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(event.getId(), result.get(0));
    }

    @Test
    void getEventIdsByUserId_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userEventService.getEventIdsByUserId(userId));
    }
}
