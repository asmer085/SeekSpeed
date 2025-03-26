package com.example.events.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity // This tells Hibernate to make a table out of this class
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Use UUID for the ID

    private String street;
    private String city;
    private String country;
    private String category;
    private UUID organizerID; // Assuming organizerID is also a UUID
    private String name;
    private String description;
    private LocalDateTime dateTime; // Combined date and time


    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UUID getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(UUID organizerID) {
        this.organizerID = organizerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}