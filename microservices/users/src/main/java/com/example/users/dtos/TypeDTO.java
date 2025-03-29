package com.example.users.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class TypeDTO {
    @NotBlank(message = "Distance is required")
    @Size(max = 100, message = "Distance cannot be longer than 100 characters")
    @Column()
    private String distance;

    @Size(max = 500, message = "Results cannot be longer than 500 characters")
    @Column()
    private String results;

    @Column()
    private UUID eventUuid;

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getResults() { return results; }

    public void setResults(String results) { this.results = results; }

    public UUID getUuid() { return eventUuid; }

    public void setUuid(UUID eventUuid) { this.eventUuid = eventUuid; }
}
