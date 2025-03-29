package com.example.users.controllers;

import com.example.users.dtos.UserDTO;
import com.example.users.entity.Users;
import com.example.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService; // Mocking the UserService

    @InjectMocks
    private UserController userController; // Injecting the mock into the controller

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private Users testUser;
    private UserDTO testUserDTO;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = new Users();
        testUser.setId(testUserId);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmailAddress("john.doe@example.com");
        testUser.setPassword("ValidPass123");
        testUser.setGender("Male");

        testUserDTO = new UserDTO();
        testUserDTO.setFirstName("John");
        testUserDTO.setLastName("Doe");
        testUserDTO.setEmailAddress("john.doe@example.com");
        testUserDTO.setGender("Male"); // required field

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsers_ShouldReturnUsers() throws Exception {
        // Arrange
        List<Users> usersList = Collections.singletonList(testUser);
        given(userService.getAllUsers()).willReturn(usersList);

        // Act & Assert
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getUserById_ExistingId_ShouldReturnUser() throws Exception {
        // Arrange
        given(userService.getUserById(testUserId)).willReturn(testUser);

        // Act & Assert
        mockMvc.perform(get("/users/{userId}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getUserById_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(userService.getUserById(testUserId))
                .willThrow(new UserService.UserNotFoundException("User not found"));

        // Act & Assert
        mockMvc.perform(get("/users/{userId}", testUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ValidUser_ShouldReturnCreatedUser() throws Exception {
        // Arrange
        testUserDTO.setGender("Male"); // Add required gender field
        given(userService.createUser(any(UserDTO.class))).willReturn(testUserDTO);

        // Act & Assert
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
    @Test
    void updateUser_ExistingUser_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        testUser.setFirstName("Updated");
        given(userService.updateUser(eq(testUserId), any(Users.class)))
                .willReturn(ResponseEntity.ok(testUser));

        // Act & Assert
        mockMvc.perform(put("/users/{userId}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void updateUser_NonExistingUser_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(userService.updateUser(eq(testUserId), any(Users.class)))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(put("/users/{userId}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_ExistingUser_ShouldReturnOk() throws Exception {
        // Arrange
        given(userService.deleteUser(testUserId))
                .willReturn(ResponseEntity.ok().build());

        // Act & Assert
        mockMvc.perform(delete("/users/{userId}", testUserId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_NonExistingUser_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(userService.deleteUser(testUserId))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(delete("/users/{userId}", testUserId))
                .andExpect(status().isNotFound());
    }
}
