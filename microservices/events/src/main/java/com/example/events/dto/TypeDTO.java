package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeDTO {
    private double price;
    private String distance;
    private String results;

    @NotNull(message = "Event ID cannot be null")
    private UUID eventId;
}