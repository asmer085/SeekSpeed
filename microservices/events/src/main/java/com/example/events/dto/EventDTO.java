package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UUID organizerID;
    private String name;
    private String description;
    private LocalDateTime dateTime;
}