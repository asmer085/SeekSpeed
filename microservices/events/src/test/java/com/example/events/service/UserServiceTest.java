package com.example.events.service;

import com.example.events.entity.User;
import com.example.events.exception.ResourceNotFoundException;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        Iterable<User> result = userService.getAllUsers();

        assertTrue(result.iterator().hasNext());
    }

    @Test
    void getUserById_ValidId_ShouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void getUserById_NotFound_ShouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }
}
