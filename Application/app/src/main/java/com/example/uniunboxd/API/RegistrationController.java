package com.example.uniunboxd.API;

import com.example.uniunboxd.DTO.RegisterModel;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * The RegistrationController class is responsible for handling user registration.
 * It provides a method to register a new user with the provided credentials and user type.
 */
public class RegistrationController {
    /**
     * Default constructor.
     */
    public RegistrationController() {

    }

    /**
     * Registers a new user with the provided credentials and user type.
     * It sends a POST request to the "Registration" endpoint with the user's email, password, and user type.
     * If the response code is not 200, it throws an exception with the error message from the server.
     * @param model The RegisterModel containing the user's email, password, and user type.
     * @throws Exception If the response code is not 200.
     */
    public static void register(RegisterModel model) throws Exception {
        JSONObject json = new JSONObject();
        json.put("email", model.email);
        json.put("password", model.password);
        json.put("type", model.userType);

        HttpURLConnection con = APIClient.post("Registration", json.toString(), null);
        if(con.getResponseCode() != 200)
            throw new Exception(APIClient.readStream(con.getErrorStream()));
        APIClient.processResponse(con, null);
    }
}