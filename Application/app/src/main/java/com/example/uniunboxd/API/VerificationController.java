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

/**
 * The VerificationController class is responsible for handling verification-related operations.
 * It provides methods to get pending applications, send applications, and resolve applications.
 */
public class VerificationController {
    /**
     * Default constructor.
     */
    public VerificationController() {
    }

    /**
     * Gets a list of pending applications starting from a specific ID.
     * @param startID The ID to start from.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of Application objects representing the pending applications.
     * @throws Exception If an error occurs.
     */
    public static List<Application> getPendingApplications(int startID, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.get("verify/pending?startID=" + startID, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<Application>>() {
        });
    }

    /**
     * Sends an application with the given files.
     * @param files The files to include in the application.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void sendApplication(byte[][] files, FragmentActivity f) throws Exception {
        sendApplication(files, -1, f);
    }

    /**
     * Sends an application with the given files to a specific university.
     * @param files The files to include in the application.
     * @param targetUniversityId The ID of the university to send the application to.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
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

    /**
     * Resolves an application with the given result.
     * @param id The ID of the application to resolve.
     * @param result The result of the application (true for accepted, false for rejected).
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void resolveApplication(int id, boolean result, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("userId", id);
        json.put("acceptedOrRejected", result);

        HttpURLConnection con = APIClient.put("verify/set", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, new TypeReference<List<Application>>() {
        });
    }
}
