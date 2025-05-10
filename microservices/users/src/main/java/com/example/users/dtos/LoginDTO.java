package com.example.users.dtos;

import jakarta.validation.constraints.*;

public class LoginDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 40, message = "Username must be between 5 and 40 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Password must contain at least one letter and one number")
    private String password;

    public String getUsername() {return this.username = username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
