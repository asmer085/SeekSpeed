package com.example.users.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column()
    private String distance;

    @Column()
    private String results;
    @Column()
    private UUID eventUuid;

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getResults() { return results; }

    public void setResults(String results) { this.results = results; }

    public UUID getUuid() { return eventUuid; }

    public void setUuid(UUID eventUuid) { this.eventUuid = eventUuid; }
}