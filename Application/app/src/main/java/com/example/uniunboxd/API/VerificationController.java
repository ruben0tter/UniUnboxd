package com.example.uniunboxd.API;

import android.util.Base64;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.models.Application;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class VerificationController {
    public static List<Application> getPendingApplications(int startID, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.get("verify/pending?startID=" + startID, JWTValidation.getToken(f));

        String body = APIClient.readStream(con.getInputStream());

        Log.d("APP", body);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(body, new TypeReference<List<Application>>() {
        });
    }

    public static void sendApplication(byte[][] files, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        JSONArray jsonData = new JSONArray();
        for (byte[] file : files) {
            if (file == null) continue;

            String base64 = Base64.encodeToString(file, Base64.DEFAULT);
            jsonData.put(base64);
        }
        json.put("verificationData", jsonData);
        json.put("targetUniversityId", 2);

        Log.d("DASdasnjkda", json.toString());

        HttpURLConnection con = APIClient.post("verify/request", json.toString(), JWTValidation.getToken(f));

        if (con.getResponseCode() == 200) {
            String body = APIClient.readStream(con.getInputStream());

            Log.d("APP1", body);
        } else {
            String test = APIClient.readStream(con.getErrorStream());
            Log.d("PLS", test);
            throw new IOException("Failed to send application");
        }

    }
}
