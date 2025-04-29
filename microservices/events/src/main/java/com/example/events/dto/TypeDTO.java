package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeDTO {
    private UUID id;
    private double price;
    private String distance;
    private String results;
    private UUID eventId;
}