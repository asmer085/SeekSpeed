package com.example.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column()
    private double price;
    private String distance;
    private String results;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Constructor without ID (for normal use)
    public Type(double price, String distance, String results, Event event) {
        this.price = price;
        this.distance = distance;
        this.results = results;
        this.event = event;
    }

    public Type(String distance, String results, Event event) {
        this.distance = distance;
        this.results = results;
        this.event = event;
    }

    // Constructor with ID (for testing or existing data)
    public Type(UUID id, double price, String distance, String results, Event event) {
        this.id = id;
        this.price = price;
        this.distance = distance;
        this.results = results;
        this.event = event;
    }
}