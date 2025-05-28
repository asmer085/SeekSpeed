package com.example.users.services;

import com.example.users.dto.UserDTO;
import com.example.users.entity.Users;
import com.example.users.mappers.UserMapper;
import com.example.users.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

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

    @Mock
    private Validator validator;

    @Mock
    private ObjectMapper objectMapper;

    private Users testUser;
    private UserDTO testUserDTO;
    private UserDTO updatedUserDTO;
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

        updatedUserDTO = new UserDTO();
        updatedUserDTO.setFirstName("Updated");
        updatedUserDTO.setLastName("Doe");
        updatedUserDTO.setEmailAddress("john.doe@example.com");
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

   /* @Test
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
    }*/

    /*@Test
    void updateUser_ExistingUser_ShouldReturnUpdatedUser() {
        // Arrange
        Users updatedUserEntity = new Users();
        updatedUserEntity.setTypeId(testUserId);
        updatedUserEntity.setFirstName("Updated");
        updatedUserEntity.setLastName("Doe");
        updatedUserEntity.setEmailAddress("john.doe@example.com");

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(Users.class))).thenReturn(updatedUserEntity);

        // Act
        ResponseEntity<Users> result = userService.updateUser(testUserId, updatedUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals("Updated", result.getBody().getFirstName());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(any(Users.class));
    }*/

    @Test
    void updateUser_NonExistingUser_ShouldReturnNotFound() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Users> result = userService.updateUser(testUserId, updatedUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, never()).save(any(Users.class));
    }

   /* @Test
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
    }*/

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

    /*@Test
    @Transactional
    void createUsersBatch_ValidUsers_ShouldReturnListOfUserDTOs() {
        // Arrange
        List<UserDTO> inputDTOs = Arrays.asList(testUserDTO, testUserDTO);
        List<Users> savedUsers = Arrays.asList(testUser, testUser);

        when(userMapper.userDTOToUsers(any(UserDTO.class))).thenReturn(testUser);
        when(userRepository.saveAll(anyList())).thenReturn(savedUsers);
        when(userMapper.usersToUserDTO(any(Users.class))).thenReturn(testUserDTO);

        // Act
        List<UserDTO> result = userService.createUsersBatch(inputDTOs);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).saveAll(anyList());
    }*/

    @Test
    @Transactional
    void createUsersBatch_InvalidUser_ShouldLogValidationError() {
        // Arrange
        testUserDTO.setFirstName("r"); // Invalid first name
        List<UserDTO> inputDTOs = Arrays.asList(testUserDTO);

        Users invalidUser = new Users();
        invalidUser.setFirstName("r"); // Takođe nevalidno

        when(userMapper.userDTOToUsers(any(UserDTO.class))).thenReturn(invalidUser);

        // Simulacija validacione greške
        ConstraintViolation<Users> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("First name must be at least 2 characters long");

        Set<ConstraintViolation<Users>> violations = new HashSet<>();
        violations.add(violation);

        when(validator.validate(any(Users.class))).thenReturn(violations);

        // Act
        try {
            userService.createUsersBatch(inputDTOs);
            System.out.println("No exception was thrown.");
        } catch (Exception e) {
            System.out.println("Exception thrown: " + e.getClass().getSimpleName());
            System.out.println("Exception message: " + e.getMessage());
        }

        // Verify validation was called
        verify(validator).validate(any(Users.class));
    }

   /* @Test
    @Transactional
    void applyPatchToUser_ValidPatch_ShouldReturnPatchedUser() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/firstName\",\"value\":\"Patched\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchJson));

        testUser.setFirstName("OldName"); // Osiguraj da korisnik ima vrednost pre patch-a

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Pravi JSON čvorovi umesto mock-a
        ObjectMapper realMapper = new ObjectMapper();
        JsonNode userNode = realMapper.valueToTree(testUser);
        JsonNode patchedNode = patch.apply(userNode);
        Users patchedUser = realMapper.treeToValue(patchedNode, Users.class);

        when(objectMapper.valueToTree(any(Users.class))).thenReturn(userNode);
        when(objectMapper.treeToValue(any(JsonNode.class), eq(Users.class))).thenReturn(patchedUser);

        when(validator.validate(patchedUser)).thenReturn(Collections.emptySet());
        when(userRepository.save(patchedUser)).thenReturn(patchedUser);

        // Act
        Users result = userService.applyPatchToUser(patch, testUserId);

        // Assert
        assertNotNull(result);
        assertEquals("Patched", result.getFirstName());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(patchedUser);
    }*/

    @Test
    @Transactional
    void applyPatchToUser_InvalidPatchOperation_ShouldThrowException() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"value\"}]";
        JsonPatch patch = new ObjectMapper().readValue(invalidPatchJson, JsonPatch.class);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.applyPatchToUser(patch, testUserId);
        });
    }

    @Test
    @Transactional
    void applyPatchToUser_NonExistingUser_ShouldThrowException() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/firstName\",\"value\":\"Patched\"}]";
        JsonPatch patch = new ObjectMapper().readValue(patchJson, JsonPatch.class);

        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserService.UserNotFoundException.class, () -> {
            userService.applyPatchToUser(patch, testUserId);
        });
    }
}