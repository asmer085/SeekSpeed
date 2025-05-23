package com.example.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int stars;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private UUID userUUID;

    // Constructor without ID (for normal use)
    public Review(int stars, Event event, UUID userUUID) {
        this.stars = stars;
        this.event = event;
        this.userUUID = userUUID;
    }

    // Constructor with ID (for testing or existing data)
    public Review(UUID id, int stars, Event event, UUID userUUID) {
        this.id = id;
        this.stars = stars;
        this.event = event;
        this.userUUID = userUUID;
    }
}