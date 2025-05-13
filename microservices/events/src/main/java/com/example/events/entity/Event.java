package com.example.events.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String street;
    private String city;
    private String country;
    private String category;

    @NotNull(message = "Organizer ID cannot be null")
    private UUID organizerID;
    private String name;
    private String description;
    private LocalDateTime dateTime;
}