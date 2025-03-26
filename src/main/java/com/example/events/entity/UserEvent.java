package com.example.events.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class UserEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Primary key

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Many UserEvents belong to one User

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event; // Many UserEvents belong to one Event

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}