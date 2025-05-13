package com.example.events.external;

import lombok.Data;

import java.util.UUID;

@Data
public class Users {
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String picture;
    private String gender;
    private String tshirtSize;
    private String country;
}
