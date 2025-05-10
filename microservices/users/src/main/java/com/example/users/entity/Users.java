package com.example.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 40, message = "Username must be between 5 and 40 characters")
    private String username;

    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email format")
    private String emailAddress;

    private String password;

    @Column()
    private String role;

    @Column()
    private String picture;

    @Column()
    private String dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be 'Male', 'Female' or 'Other'")
    private String gender;

    @Column()
    @Pattern(regexp = "XS|S|M|L|XL|XXL", message = "Invalid t-shirt size")
    private String tShirtSize;

    @Column()
    private String organisationFile;

    @Column()
    private String country;


    public UUID getId() { return id; }

    public void setId(UUID uuid) { this.id = uuid; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() {return this.username = username;}

    public void setUsername(String username) {this.username = username;}

    public String getEmailAddress() { return emailAddress; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getPicture() { return picture; }

    public void setPicture(String picture) { this.picture = picture; }

    public String getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getTShirtSize() { return tShirtSize; }

    public void setTShirtSize(String tShirtSize) { this.tShirtSize = tShirtSize; }

    public String getOrganisationFile() { return organisationFile; }

    public void setOrganisationFile(String organisationFile) { this.organisationFile = organisationFile; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }
}
