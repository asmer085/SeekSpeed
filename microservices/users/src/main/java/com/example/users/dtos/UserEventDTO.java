package com.example.users.dtos;

import com.example.users.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

public class UserEventDTO {

    private UUID eventID;

    private Users user;

    public UUID getEventID() {return eventID;}

    public void setEventID(UUID eventID) {this.eventID = eventID;}

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
