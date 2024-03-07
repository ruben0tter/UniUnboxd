package com.example.uniunboxd.DTO;

public class RegisterModel {
    public String email;
    public String password;
    public String userType;

    public RegisterModel(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
