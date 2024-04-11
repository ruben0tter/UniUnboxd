package com.example.uniunboxd.DTO;

/**
 * The RegisterModel class represents the data required for user registration.
 * It includes an email, a password, and a user type.
 */
public class RegisterModel {
    /**
     * The email of the user.
     */
    public String email;

    /**
     * The password of the user.
     */
    public String password;

    /**
     * The type of the user (e.g., "student", "professor").
     */
    public String userType;

    /**
     * Constructs a new RegisterModel with the specified email, password, and user type.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param userType The type of the user.
     */
    public RegisterModel(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
