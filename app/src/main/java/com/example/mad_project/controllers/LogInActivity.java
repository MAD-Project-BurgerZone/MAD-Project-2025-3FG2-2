package com.example.mad_project.controllers;

import com.example.mad_project.models.User;
public class LogInActivity {
    private final User testUser = new User("test@example.com", "john_doe", "12345");
    public void loginUser(String email, String password){
        if (testUser.getEmail().equals(email) && testUser.getPassword().equals(password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }
}
