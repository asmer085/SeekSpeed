package com.example.users.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Newsletter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Users getUser() { return user; }

    public void setUser(Users user) { this.user = user; }
}
