package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.utilities.JWTValidation;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class UserController {
    public static HttpURLConnection setDeviceToken(String deviceToken, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("deviceToken", deviceToken);

        return APIClient.put("User/set-device-token", json.toString(), JWTValidation.getToken(f));
    }

    public static HttpURLConnection follow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/follow?id=" + id, null, JWTValidation.getToken(f));
    }

    public static HttpURLConnection unfollow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/unfollow?id=" + id, null, JWTValidation.getToken(f));
    }
}
