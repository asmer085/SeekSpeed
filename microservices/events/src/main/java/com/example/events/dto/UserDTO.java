package com.example.events.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String picture;
    private String dateOfBirth;
    private String gender;
    private String country;
    private String tShirtSize;
    private UUID userId;
}