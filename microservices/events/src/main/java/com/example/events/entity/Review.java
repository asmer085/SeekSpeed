package com.example.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int stars;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private UUID userUUID;
}