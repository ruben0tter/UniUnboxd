package com.example.uniunboxd.API;

import com.example.uniunboxd.DTO.AuthenticationModel;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class AuthenticationController {
    public static HttpURLConnection authenticate(AuthenticationModel model) throws Exception {
        JSONObject json = new JSONObject();
        json.put("email", model.email);
        json.put("password", model.password);

        return APIClient.post("Authentication", json.toString(), null);
    }
}
