package com.example.uniunboxd.DTO;

public class AuthenticationModel {
    public String email;
    public String password;

    public AuthenticationModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
