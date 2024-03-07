package com.example.uniunboxd.API;

import android.util.Log;

import com.example.uniunboxd.DTO.RegisterModel;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RegistrationController {
    public static HttpURLConnection register(RegisterModel model) throws Exception {
        JSONObject json = new JSONObject();
        json.put("email", model.email);
        json.put("password", model.password);
        json.put("type", model.userType);

        return APIClient.post("Registration", json.toString());
    }
}
