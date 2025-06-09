package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class UserEventDTO {
    private UUID userId;
    private UUID eventId;

    public UserEventDTO(UUID id, UUID eventId) {
        this.eventId = eventId;
        this.userId = id;
    }

    public UserEventDTO() {}

    public UUID getEventId() {return eventId;}

    public void setEventId(UUID eventID) {this.eventId = eventID;}

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}