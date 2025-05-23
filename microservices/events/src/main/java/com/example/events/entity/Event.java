package com.example.events.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String street;
    private String city;
    private String country;
    private String category;
    private UUID organizerID;
    private String name;
    private String description;
    private String date;
    private String time;

    // Constructor with all fields except ID (since it's auto-generated)
    public Event(String street, String city, String country, String category,
                 UUID organizerID, String name, String description,
                 String date, String time) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.category = category;
        this.organizerID = organizerID;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    // Alternative constructor with ID (useful for testing or when you need to set ID manually)
    public Event(UUID id, String street, String city, String country, String category,
                 UUID organizerID, String name, String description,
                 String date, String time) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.country = country;
        this.category = category;
        this.organizerID = organizerID;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
    }
}