package com.example.uniunboxd.API;

import android.util.Base64;
import android.util.Log;

import com.example.uniunboxd.Application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class VerificationController {
    public static List<Application> getPendingApplications(int startID) throws Exception {
        HttpURLConnection con = APIClient.get("verify/pending?startID=" + startID);

        StringBuilder body = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                body.append(responseLine);
            }
        }

        Log.d("APP", body.toString());

        List<Application> res = new ArrayList<>();

        JSONArray json = new JSONArray(body.toString());
        for (int i = 0; i < json.length(); i++) {
            throw new Exception("TODO: need a functioning server first");
        }

        return res;
    }

    public static void sendApplication(List<byte[]> files) throws Exception {
        JSONObject json = new JSONObject();
        JSONArray jsonData = new JSONArray();
        for (byte[] file : files) {
            String base64 = Base64.encodeToString(file, Base64.DEFAULT);
            jsonData.put(base64);
        }
        json.put("VerificationData", jsonData);
        json.put("TargetUniversity", 69);

        HttpURLConnection con = APIClient.post("verify/request", json.toString());
    }
}
