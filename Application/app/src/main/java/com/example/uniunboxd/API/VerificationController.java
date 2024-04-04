package com.example.uniunboxd.API;

import android.util.Base64;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.models.Application;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;

public class VerificationController {
    public VerificationController() {
    }

    public static List<Application> getPendingApplications(int startID, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.get("verify/pending?startID=" + startID, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<Application>>() {
        });
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
        APIClient.processResponse(con, new TypeReference<List<Application>>() {
        });
    }

    public static void resolveApplication(int id, boolean result, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("userId", id);
        json.put("acceptedOrRejected", result);

        HttpURLConnection con = APIClient.put("verify/set", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, new TypeReference<List<Application>>() {
        });
    }
}
