package com.example.users.dto;

import java.io.Serializable;
import java.util.UUID;


public class EventUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String picture;
    private String dateOfBirth;
    private String gender;
    private String country;
    private String tShirtSize;
    private UUID id;

    public EventUserDTO() {
    }

    public EventUserDTO(String firstName, String lastName, String username,
                        String emailAddress, String picture, String dateOfBirth,
                        String gender, String country, String tShirtSize, UUID id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.country = country;
        this.tShirtSize = tShirtSize;
        this.id = id;
    }

    public UUID getUserId() {
        return this.id;
    }

    public void setUserId(UUID userId) {
        this.id = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTShirtSize() {
        return this.tShirtSize;
    }

    public void setTShirtSize(String tShirtSize) {
        this.tShirtSize = tShirtSize;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "EventUserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", picture='" + picture + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", tShirtSize='" + tShirtSize + '\'' +
                ", userId=" + id +
                '}';
    }
}