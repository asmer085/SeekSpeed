package com.example.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private double price;
    private String distance;
    private String results;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}