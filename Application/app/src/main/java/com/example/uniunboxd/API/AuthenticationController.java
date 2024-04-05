package com.example.uniunboxd.API;

import com.example.uniunboxd.DTO.AuthenticationModel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class AuthenticationController {
    private AuthenticationController() {

    }

    public static class AuthenticationResult {
        String token;

        @JsonCreator
        public AuthenticationResult(@JsonProperty("token") String token) {
            this.token = token;
        }
    }

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
