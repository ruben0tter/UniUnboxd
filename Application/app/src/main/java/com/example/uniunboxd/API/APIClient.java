package com.example.uniunboxd.API;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APIClient {
    public APIClient() {
    }

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
        catch(Exception e){
            Log.e("AAAAAAA", e.toString());
        }

        return con;
    }

    public static HttpURLConnection put(String url, String body, String token) throws IOException {
        HttpURLConnection con = fetch("PUT", url, token);

        if (body != null) {
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        return con;
    }

    public static HttpURLConnection delete(String url, String token) throws IOException {
        return fetch("DELETE", url, token);
    }

    private static HttpURLConnection fetch(String method, String url, String token) throws IOException {
        URL urlObj = new URL("https://uniunboxd.com/api/" + url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + (token != null ? token : ""));
        con.setRequestMethod(method);
        con.setRequestProperty("Accept", "application/json");
//        con.setConnectTimeout(5000);
//        con.setReadTimeout(5000);
        return con;
    }

    public static String readStream(InputStream stream) {
        StringBuilder body = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                body.append(responseLine);
            }
        } catch (Exception e) {
            Log.e("ERR", e.toString());
        }

        return body.toString();
    }


    public static <T> T decodeJson(InputStream stream, TypeReference<T> t) throws IOException {
        if (t == null) return null;
        return new ObjectMapper().readValue(stream, t);
    }

    public static <T> T processResponse(HttpURLConnection con, TypeReference<T> t) throws IOException {
        if (con.getResponseCode() == 200) {
            return decodeJson(con.getInputStream(), t);
        } else {
            String error = readStream(con.getErrorStream());
            Log.d("API", "Received unexpected response code:" + con.getResponseCode() + "\n" + error);
            throw new IOException("Received unexpected response code:" + con.getResponseCode());
        }
    }
}
