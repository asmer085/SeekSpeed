package com.example.users.controllers;

import com.example.users.dtos.UserDTO;
import com.example.users.entity.Users;
import com.example.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
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
        testUserDTO.setPassword("ValidPass123");
        testUserDTO.setGender("Male");

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
        given(userService.updateUser(eq(testUserId), any(UserDTO.class)))
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
        given(userService.updateUser(eq(testUserId), any(UserDTO.class)))
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

    @Test
    void createUsersBatch_ValidUsers_ShouldReturnCreatedUsers() throws Exception {
        // Arrange
        List<UserDTO> usersDTO = List.of(testUserDTO, testUserDTO);
        List<UserDTO> createdUsers = List.of(testUserDTO, testUserDTO);

        given(userService.createUsersBatch(anyList())).willReturn(createdUsers);

        // Act & Assert
        mockMvc.perform(post("/users/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("John"));
    }

    @Test
    void patchUpdateUser_ValidPatch_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/firstName\",\"value\":\"Patched\"}]";
        Users patchedUser = new Users();
        patchedUser.setId(testUserId);
        patchedUser.setFirstName("Patched");

        given(userService.applyPatchToUser(any(JsonPatch.class), eq(testUserId)))
                .willReturn(patchedUser);

        // Act & Assert
        mockMvc.perform(patch("/users/{userId}", testUserId)
                        .contentType("application/json-patch+json")
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Patched"));
    }

    @Test
    void patchUpdateUser_InvalidPatch_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"value\"}]";

        given(userService.applyPatchToUser(any(JsonPatch.class), eq(testUserId)))
                .willThrow(new RuntimeException("Invalid patch"));

        // Act & Assert
        mockMvc.perform(patch("/users/{userId}", testUserId)
                        .contentType("application/json-patch+json")
                        .content(invalidPatchJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid patch"));
    }

    @Test
    void createUsersBatch_InvalidUsers_ShouldReturnBadRequest() throws Exception {
        // Arrange
        testUserDTO.setFirstName(""); // Invalid first name
        List<UserDTO> usersDTO = List.of(testUserDTO);

        given(userService.createUsersBatch(anyList()))
                .willThrow(new RuntimeException("Validation failed"));

        // Act & Assert
        mockMvc.perform(post("/users/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed"));
    }
}
