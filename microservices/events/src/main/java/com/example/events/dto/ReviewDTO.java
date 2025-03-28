package com.example.events.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    @NotNull(message = "Enter number of stars")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private int stars;

    @NotNull(message = "Event ID cannot be null")
    private UUID eventId;

    @NotNull(message = "User ID cannot be null")
    private UUID userUUID;
}