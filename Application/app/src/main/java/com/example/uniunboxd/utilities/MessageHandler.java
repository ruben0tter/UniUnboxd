package com.example.uniunboxd.utilities;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class MessageHandler {

    public static void showToastFromBackground(FragmentActivity a, InputStream message) {
        a.runOnUiThread(() -> {
            try {
                Toast.makeText(a, MessageHandler.readMessage(message), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("TOAST", e.toString());
            }
        });
    }

    private static String readMessage(InputStream content) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (content, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        return textBuilder.toString().replace("\"", "");
    }
}
