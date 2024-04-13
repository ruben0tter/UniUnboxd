package com.example.uniunboxd.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * The JWTValidation class provides methods for handling JSON Web Tokens (JWTs) in the context of user authentication.
 */
public class JWTValidation {
    /**
     * Checks if a user is logged in by checking if a token exists.
     * @param c The application context.
     * @return True if a token exists, false otherwise.
     */
    public static boolean isUserLoggedIn(Context c) {
        String token = getToken(c);
        return token != null;
    }

    /**
     * Retrieves the token from shared preferences.
     * @param c The application context.
     * @return The token, or null if it does not exist.
     */
    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        return prefs.getString("token", null);
    }

    /**
     * Retrieves a property from the payload of the token.
     * @param c The application context.
     * @param key The key of the property to retrieve.
     * @return The value of the property, or null if it does not exist.
     */
    public static String getTokenProperty(Context c, String key) {
        String payload = getPayload(c);

        try {
            JSONObject obj = new JSONObject(payload);

            if (obj.has(key)) {
                return obj.get(key).toString();
            } else {
                return null;
            }
        } catch (Throwable t) {
            Log.e("JWT", "Could not parse malformed JSON: \"" + payload + "\"" + "\n\n" + t);
        }

        return null;
    }

    /**
     * Places a token into shared preferences.
     * @param token The token to place.
     * @param c The application context.
     * @throws NullPointerException If the token is null.
     */
    public static void placeToken(String token, Context c) throws NullPointerException {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("token", token);
        edit.apply();
    }

    /**
     * Deletes the token from shared preferences.
     * @param c The application context.
     */
    public static void deleteToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove("token").apply();
    }

    /**
     * Retrieves the payload from the token.
     * @param c The application context.
     * @return The payload of the token.
     */
    private static String getPayload(Context c) {
        String token = getToken(c);
        String[] chunks = token.split("\\.");

        byte[] bytes = Base64.decode(chunks[1], Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
