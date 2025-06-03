package com.example.events.dto;

import java.io.Serializable;
import java.util.UUID;

public class UserUpdateEventDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String role;
    private String dateOfBirth;
    private String gender;
    private String tShirtSize;
    private String country;
    private String picture;

    public UserUpdateEventDTO() {}

    // Constructors
    public UserUpdateEventDTO(UUID userId, String firstName, String lastName, String username, String emailAddress, String role,
                              String dateOfBirth, String gender, String tShirtSize, String country, String picture) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.tShirtSize = tShirtSize;
        this.country = country;
        this.picture = picture;
    }

    // Getters and setters...
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTShirtSize() {
        return tShirtSize;
    }

    public void setTShirtSize(String tShirtSize) {
        this.tShirtSize = tShirtSize;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
