package com.example.events.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Primary key

    private int stars; // Rating (e.g., 1 to 5 stars)

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event; // Many reviews belong to one event

    private UUID userUUID; // UUID of the user who wrote the review

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }
}