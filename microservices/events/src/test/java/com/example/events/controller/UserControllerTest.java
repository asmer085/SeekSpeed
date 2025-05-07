package com.example.events.controller;

import com.example.events.entity.User;
import com.example.events.exception.GlobalExceptionHandler;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setAddress("123 Main St");
        testUser.setPicture("profile.jpg");
        testUser.setGender("Male");
        testUser.setTShirtSize("M");
        testUser.setCountry("USA");
    }

    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(testUser));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        when(userService.getUserById(userId)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getUserById_NotFound_ShouldReturn404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(userService.getUserById(nonExistentId))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/{userId}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.errorType").value("not_found"))
                .andExpect(jsonPath("$.path").value("/api/users/" + nonExistentId.toString()));
    }

}
