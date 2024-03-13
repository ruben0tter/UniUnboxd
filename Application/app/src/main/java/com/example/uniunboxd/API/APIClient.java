package com.example.uniunboxd.API;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APIClient {
    public static HttpURLConnection fetch(String method, String url) throws IOException {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI3YzJkMTY5OS0xNDU4LTRhMDMtOGYyYS1mZDU1ZGRmNDY5OGUiLCJzdWIiOiIyIiwibmFtZSI6Im1hcnRpbmhhdGVyIiwiZW1haWwiOiJtYXJ0aW5oYXRlckBnbWFpbC5jb20iLCJ0eXAiOiJVbml2ZXJzaXR5IiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiVW5pdmVyc2l0eSIsImV4cCI6MTcxMDk1MjMzOH0.wMmVQaXncphGFkpCNTP0tWq4VAHuZCsmjRGy92UyO2Y";

        URL urlObj = new URL("http://10.0.2.2:5148/api/" + url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestProperty ("Authorization", "Bearer " + token);
        con.setRequestMethod(method);
        con.setRequestProperty("Accept", "application/json");

        return con;
    }

    public static HttpURLConnection get(String url) throws IOException {
        return fetch("GET", url);
    }

    public static HttpURLConnection post(String url, String body) throws IOException {
        HttpURLConnection con = fetch("POST", url);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return con;
    }

    public static HttpURLConnection put(String url, String body) throws IOException {
        HttpURLConnection con = fetch("PUT", url);
        con.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return con;
    }

    public static HttpURLConnection delete(String url) throws IOException {
        return fetch("DELETE", url);
    }
}
