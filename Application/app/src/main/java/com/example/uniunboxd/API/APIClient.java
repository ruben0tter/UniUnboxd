package com.example.uniunboxd.API;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APIClient {
    public static HttpURLConnection fetch(String method, String url) throws IOException {
        URL urlObj = new URL("https://uniunboxd.com/api/" + url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        return con;
    }

    public static HttpURLConnection get(String url) throws IOException {
        return fetch("GET", url);
    }

    public static HttpURLConnection post(String url, String body) throws IOException {
        HttpURLConnection con = fetch("POST", url);
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
        return fetch("GET", url);
    }
}
