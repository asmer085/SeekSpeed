package com.example.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private UUID id;

    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String picture;
    private String dateOfBirth;
    private String role;
    private String gender;
    private String country;
    private String tShirtSize;
    private UUID userId;

    public User(UUID id, String firstName, String lastName, String username, String emailAddress,
                String picture, String dateOfBirth, String role, String gender, String country, String tShirtSize,
                UUID userId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.gender = gender;
        this.country = country;
        this.tShirtSize = tShirtSize;
        this.userId = userId;
    }

    public User(String firstName, String lastName, String username, String emailAddress, String picture, String dateOfBirth, String gender, String country, String tShirtSize, UUID id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.country = country;
        this.tShirtSize = tShirtSize;
        this.userId = id;
    }

    public User() {
        // Prazan konstruktor potreban JPA-u
    }

}