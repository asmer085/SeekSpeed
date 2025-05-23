package com.example.events.external;

import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
public class Users {
    @Id
    private UUID id;

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