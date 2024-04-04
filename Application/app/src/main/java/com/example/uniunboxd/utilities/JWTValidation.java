package com.example.uniunboxd.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class JWTValidation {
    public static boolean isUserLoggedIn(Context c) {
        String token = getToken(c);
        return token != null;
    }

    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        return prefs.getString("token", null);
    }

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

    public static void deleteToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove("token").apply();
    }

    private static String getPayload(Context c) {
        String token = getToken(c);
        String[] chunks = token.split("\\.");

        byte[] bytes = Base64.decode(chunks[1], Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
