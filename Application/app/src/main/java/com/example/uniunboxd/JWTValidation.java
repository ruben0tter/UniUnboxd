package com.example.uniunboxd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Base64;

public class JWTValidation {
    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return prefs.getString("token","");
    }

    public static String getPayload(Context c) {
        String token = getToken(c);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decoder = Base64.getUrlDecoder();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String(decoder.decode(chunks[1]));
        }

        return null;
    }

    public static void deleteToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove("token").commit();
    }
}
