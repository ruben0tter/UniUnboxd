package com.example.uniunboxd.DTO;

/**
 * The AuthenticationModel class represents the credentials required for user authentication.
 * It includes an email and a password.
 */
public class AuthenticationModel {
    /**
     * The email of the user.
     */
    public String email;

    /**
     * The password of the user.
     */
    public String password;

    /**
     * Constructs a new AuthenticationModel with the specified email and password.
     * @param email The email of the user.
     * @param password The password of the user.
     */
    public AuthenticationModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
