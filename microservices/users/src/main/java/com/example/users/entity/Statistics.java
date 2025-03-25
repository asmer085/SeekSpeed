package com.example.users.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column()
    private Double averagePace;

    @Column()
    private Double bestPace;

    @Column()
    private Double totalTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public Double getAveragePace() { return averagePace; }

    public void setAveragePace(Double averagePace) { this.averagePace = averagePace; }

    public Double getBestPace() { return bestPace; }

    public void setBestPace(Double bestPace) { this.bestPace = bestPace; }

    public Double getTotalTime() { return totalTime; }

    public void setTotalTime(Double totalTime) { this.totalTime = totalTime; }

    public Users getUser() { return user; }

    public void setUser(Users user) { this.user = user; }

    public Type getType() { return type; }

    public void setType(Type type) { this.type = type; }
}