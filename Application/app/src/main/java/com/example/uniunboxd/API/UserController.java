package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.utilities.JWTValidation;

import java.net.HttpURLConnection;

public class UserController {
    public static HttpURLConnection follow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/follow?id=" + id, null, JWTValidation.getToken(f));
    }

    public static HttpURLConnection unfollow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/unfollow?id=" + id, null, JWTValidation.getToken(f));
    }
}
