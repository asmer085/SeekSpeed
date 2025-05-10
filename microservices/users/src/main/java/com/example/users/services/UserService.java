package com.example.users.services;

import com.example.users.dtos.UserDTO;
import com.example.users.entity.Users;
import com.example.users.mappers.UserMapper;
import com.example.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;

import java.util.*;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    public Iterable<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    public Users getUserByUsername(String username) {
        Users user = userRepository.findByUsername(username);
        if (user == null) throw new UserNotFoundException("User with username " + username + " not found");
        return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User Not Found with username: " + username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public UserDTO createUser(UserDTO userDTO) {
        String password = userDTO.getPassword();

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            throw new IllegalArgumentException("Password must contain at least one letter and one number");
        }

        Users user = userMapper.userDTOToUsers(userDTO);
        Users savedUser = userRepository.save(user);
        return userMapper.usersToUserDTO(savedUser);
    }


    @Transactional
    public List<UserDTO> createUsersBatch(List<UserDTO> usersDTO) {
        List<Users> users = new ArrayList<>();

        for (UserDTO userDTO : usersDTO) {
            Users user = userMapper.userDTOToUsers(userDTO);

            Set<ConstraintViolation<Users>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            users.add(user);
        }

        Iterable<Users> savedUsers = userRepository.saveAll(users);

        return users.stream()
                .map(userMapper::usersToUserDTO)
                .toList();
    }

    @Transactional
    public ResponseEntity<Users> updateUser(UUID userId, UserDTO updatedUser) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    if (updatedUser.getFirstName() != null) existingUser.setFirstName(updatedUser.getFirstName());
                    if (updatedUser.getLastName() != null) existingUser.setLastName(updatedUser.getLastName());
                    if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
                    if (updatedUser.getEmailAddress() != null) existingUser.setEmailAddress(updatedUser.getEmailAddress());
                    if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
                    if (updatedUser.getRole() != null) existingUser.setRole(updatedUser.getRole());
                    if (updatedUser.getPicture() != null) existingUser.setPicture(updatedUser.getPicture());
                    if (updatedUser.getDateOfBirth() != null) existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
                    if (updatedUser.getGender() != null) existingUser.setGender(updatedUser.getGender());
                    if (updatedUser.getTShirtSize() != null) existingUser.setTShirtSize(updatedUser.getTShirtSize());
                    if (updatedUser.getOrganisationFile() != null) existingUser.setOrganisationFile(updatedUser.getOrganisationFile());
                    if (updatedUser.getCountry() != null) existingUser.setCountry(updatedUser.getCountry());

                    return ResponseEntity.ok(userRepository.save(existingUser));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public Users applyPatchToUser(JsonPatch patch, UUID userId) {
        try {
            Users user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
            JsonNode userNode = objectMapper.valueToTree(user);
            JsonNode patchedNode = patch.apply(userNode);
            Users patchedUser = objectMapper.treeToValue(patchedNode, Users.class);

            Set<ConstraintViolation<Users>> violations = validator.validate(patchedUser);

            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<Users> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(" ");
                }
                throw new RuntimeException(errorMessage.toString());
            }
            return userRepository.save(patchedUser);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteUser(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
