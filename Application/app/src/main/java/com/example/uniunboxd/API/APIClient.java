package com.example.uniunboxd.API;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APIClient {
    public static HttpURLConnection get(String url, String token) throws IOException {
        return fetch("GET", url, token);
    }

    public static HttpURLConnection post(String url, String body, String token) throws IOException {
        HttpURLConnection con = fetch("POST", url, token);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return con;
    }

    public static HttpURLConnection put(String url, String body, String token) throws IOException {
        HttpURLConnection con = fetch("PUT", url, token);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return con;
    }

    public static HttpURLConnection delete(String url, String token) throws IOException {
        return fetch("DELETE", url, token);
    }

    private static HttpURLConnection fetch(String method, String url, String token) throws IOException {
        URL urlObj = new URL("http://10.0.2.2:5148/api/" + url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestProperty ("Authorization", "Bearer " + (token != null ? token : ""));
        con.setRequestMethod(method);
        con.setRequestProperty("Accept", "application/json");

        return con;
    }

}
