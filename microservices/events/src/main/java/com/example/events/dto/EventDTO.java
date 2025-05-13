package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String street;
    private String city;
    private String country;
    private String category;

    @NotNull(message = "Organizer ID cannot be null")
    private UUID organizerID;

    @NotNull(message = "Name cannot be null")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Future(message = "Event date must be in the future")
    private LocalDateTime dateTime;
}