package com.example.events.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Primary key

    private double price; // Price of the event type
    private String distance; // Distance (e.g., "5km", "10km")
    private String results; // Results (e.g., "Winner", "Runner-up")

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event; // Many types belong to one event

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}