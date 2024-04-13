package com.example.uniunboxd.API;

import com.example.uniunboxd.DTO.AuthenticationModel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * The AuthenticationController class is responsible for handling user authentication.
 * It contains a nested class, AuthenticationResult, which represents the result of an authentication attempt.
 * The authenticate method is used to authenticate a user with the provided credentials.
 */
public class AuthenticationController {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AuthenticationController() {

    }

    /**
     * The AuthenticationResult class represents the result of an authentication attempt.
     * It contains a token which is set upon successful authentication.
     */
    public static class AuthenticationResult {
        String token;

        /**
         * Constructor for the AuthenticationResult class.
         * @param token The token to set.
         */
        @JsonCreator
        public AuthenticationResult(@JsonProperty("token") String token) {
            this.token = token;
        }
    }

    /**
     * Authenticates a user with the provided credentials.
     * It sends a POST request to the "Authentication" endpoint with the user's email and password.
     * If the response code is not 200, it throws an exception with the error message from the server.
     * If the response code is 200, it processes the response and returns the token.
     * @param model The AuthenticationModel containing the user's email and password.
     * @return The token upon successful authentication.
     * @throws Exception If the response code is not 200.
     */
    public static String authenticate(AuthenticationModel model) throws Exception {
        JSONObject json = new JSONObject();
        json.put("email", model.email);
        json.put("password", model.password);

        HttpURLConnection con = APIClient.post("Authentication", json.toString(), null);
        if(con.getResponseCode() != 200)
            throw new Exception(APIClient.readStream(con.getErrorStream()));
        return APIClient.processResponse(con, new TypeReference<AuthenticationResult>() {
        }).token;
    }
}
