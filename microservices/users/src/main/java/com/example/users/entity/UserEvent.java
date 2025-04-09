package com.example.users.entity;


import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class UserEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column()
    private UUID eventID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventID() {return eventID;}

    public void setEventID(UUID eventID) {this.eventID = eventID;}

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}