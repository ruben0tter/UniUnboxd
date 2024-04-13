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

/**
 * APIClient is a utility class that provides methods for making HTTP requests.
 * It supports GET, POST, PUT, DELETE methods and handles the connection setup, request sending, and response processing.
 */
public class APIClient {
    /**
     * Default constructor.
     */
    public APIClient() {
    }

    /**
     * Makes a GET request to the specified URL with the provided token.
     * @param url The URL to make the request to.
     * @param token The token to include in the request header.
     * @return The HttpURLConnection object representing the connection to the URL.
     * @throws IOException If an I/O error occurs.
     */
    public static HttpURLConnection get(String url, String token) throws IOException {
        return fetch("GET", url, token);
    }

    /**
     * Makes a POST request to the specified URL with the provided body and token.
     * @param url The URL to make the request to.
     * @param body The body to include in the request.
     * @param token The token to include in the request header.
     * @return The HttpURLConnection object representing the connection to the URL.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Makes a PUT request to the specified URL with the provided body and token.
     * @param url The URL to make the request to.
     * @param body The body to include in the request.
     * @param token The token to include in the request header.
     * @return The HttpURLConnection object representing the connection to the URL.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Makes a DELETE request to the specified URL with the provided token.
     * @param url The URL to make the request to.
     * @param token The token to include in the request header.
     * @return The HttpURLConnection object representing the connection to the URL.
     * @throws IOException If an I/O error occurs.
     */
    public static HttpURLConnection delete(String url, String token) throws IOException {
        return fetch("DELETE", url, token);
    }

    /**
     * Sets up a connection to the specified URL with the provided method and token.
     * @param method The HTTP method to use.
     * @param url The URL to make the request to.
     * @param token The token to include in the request header.
     * @return The HttpURLConnection object representing the connection to the URL.
     * @throws IOException If an I/O error occurs.
     */
    private static HttpURLConnection fetch(String method, String url, String token) throws IOException {
        URL urlObj = new URL("https://uniunboxd.com/api/" + url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + (token != null ? token : ""));
        con.setRequestMethod(method);
        con.setRequestProperty("Accept", "application/json");
        return con;
    }

    /**
     * Reads the content of the provided InputStream into a String.
     * @param stream The InputStream to read from.
     * @return The content of the InputStream as a String.
     */
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

    /**
     * Decodes the content of the provided InputStream into an object of the specified type.
     * @param stream The InputStream to read from.
     * @param t The TypeReference representing the type to decode into.
     * @return The decoded object.
     * @throws IOException If an I/O error occurs.
     */
    public static <T> T decodeJson(InputStream stream, TypeReference<T> t) throws IOException {
        if (t == null) return null;
        return new ObjectMapper().readValue(stream, t);
    }

    /**
     * Processes the response of the provided HttpURLConnection.
     * If the response code is 200, it decodes the response into an object of the specified type.
     * Otherwise, it reads the error stream and throws an IOException.
     * @param con The HttpURLConnection to process the response of.
     * @param t The TypeReference representing the type to decode into.
     * @return The decoded object.
     * @throws IOException If an I/O error occurs or the response code is not 200.
     */
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
