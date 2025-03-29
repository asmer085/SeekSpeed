package com.example.users.services;

import com.example.users.dtos.UserDTO;
import com.example.users.entity.Users;
import com.example.users.mappers.UserMapper;
import com.example.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

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

        testUserDTO = new UserDTO();
        testUserDTO.setFirstName("John");
        testUserDTO.setLastName("Doe");
        testUserDTO.setEmailAddress("john.doe@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        // Act
        Iterable<Users> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Users>) result).size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ExistingId_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Act
        Users result = userService.getUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    void getUserById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserService.UserNotFoundException.class, () -> {
            userService.getUserById(testUserId);
        });
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    void createUser_ValidUser_ShouldReturnUserDTO() {
        // Arrange
        when(userMapper.userDTOToUsers(any(UserDTO.class))).thenReturn(testUser);
        when(userRepository.save(any(Users.class))).thenReturn(testUser);
        when(userMapper.usersToUserDTO(any(Users.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.createUser(testUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDTO, result);
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void updateUser_ExistingUser_ShouldReturnUpdatedUser() {
        // Arrange
        Users updatedUser = new Users();
        updatedUser.setFirstName("Updated");

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(Users.class))).thenReturn(updatedUser);

        // Act
        ResponseEntity<Users> result = userService.updateUser(testUserId, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void updateUser_NonExistingUser_ShouldReturnNotFound() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Users> result = userService.updateUser(testUserId, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void deleteUser_ExistingUser_ShouldReturnOk() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<Object> result = userService.deleteUser(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteUser_NonExistingUser_ShouldReturnNotFound() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> result = userService.deleteUser(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, never()).delete(any(Users.class));
    }
}