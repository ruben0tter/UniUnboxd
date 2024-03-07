package com.example.uniunboxd.API;

import android.util.Log;

import com.example.uniunboxd.DTO.AuthenticationModel;
import com.example.uniunboxd.DTO.RegisterModel;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class AuthenticationController {
    public static HttpURLConnection authenticate(AuthenticationModel model) throws Exception {
        JSONObject json = new JSONObject();
        json.put("email", model.email);
        json.put("password", model.password);

        return APIClient.post("Authentication", json.toString());
    }
}
