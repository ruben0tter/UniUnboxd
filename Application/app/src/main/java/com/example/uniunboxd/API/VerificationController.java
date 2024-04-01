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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class VerificationController {
    public static List<Application> getPendingApplications(int startID, FragmentActivity f) throws Exception {
        try {
            HttpURLConnection con = APIClient.get("verify/pending?startID=" + startID, JWTValidation.getToken(f));

            String body = APIClient.readStream(con.getInputStream());

            Log.d("APP", body);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body, new TypeReference<List<Application>>() {
            });
        } catch (Exception e) {
            Log.e("NIG", e.toString());
        }

        return new ArrayList<>();
    }

    public static void sendApplication(byte[][] files, FragmentActivity f) throws Exception {
        sendApplication(files, -1, f);
    }

    public static void sendApplication(byte[][] files, int targetUniversityId, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        JSONArray jsonData = new JSONArray();
        for (byte[] file : files) {
            if (file == null) continue;

            String base64 = Base64.encodeToString(file, Base64.DEFAULT);
            jsonData.put(base64);
        }
        json.put("verificationData", jsonData);
        if (targetUniversityId != -1) {
            json.put("targetUniversityId", targetUniversityId);
        }

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

    public static void resolveApplication(int id, boolean result, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();

        json.put("userId", id);
        json.put("acceptedOrRejected", result);

        Log.d("POG", json.toString());

        HttpURLConnection con = APIClient.put("verify/set", json.toString(), JWTValidation.getToken(f));

        if (con.getResponseCode() == 200) {
            String body = APIClient.readStream(con.getInputStream());

            Log.d("APP1", body);
        } else {
            String test = APIClient.readStream(con.getErrorStream());
            Log.d("PLS", test);
            Log.d("PLS-code", "" + con.getResponseCode());
            throw new IOException("Failed to accept/reject application");
        }
    }

    public static HttpURLConnection sendApplication(List<byte[]> files, int universityId, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        JSONArray jsonData = new JSONArray();
        for (byte[] file : files) {
            String base64 = Base64.encodeToString(file, Base64.DEFAULT);
            jsonData.put(base64);
        }
        json.put("verificationData", jsonData);
        json.put("targetUniversityId", universityId);

        HttpURLConnection con = APIClient.post("verify/request", json.toString(), JWTValidation.getToken(f));

        return con;
    }
}
