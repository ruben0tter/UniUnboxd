package com.example.uniunboxd.API;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.models.CourseRetrievalModel;
import com.example.uniunboxd.models.ReviewListItem;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReviewController {
    public static HttpURLConnection postReview(ReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("rating", model.rating);
        json.put("comment", model.comment);
        json.put("isAnonymous", model.isAnonymous);
        json.put("courseId", model.courseId);
        json.put("studentId", model.studentId);

        return APIClient.post("Review", json.toString(), JWTValidation.getToken(f));
    }

    public static List<ReviewListItem> getReviewListItems(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con =  APIClient.get("Review/get-next-reviews?id="+id, JWTValidation.getToken(f));

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("ERR", e.toString());
            }
        } else {
            String test = readMessage(con.getErrorStream());
            Log.d("PLS", test);
        }

        Log.d("APP1", body.toString());
        ObjectMapper objectMapper = new ObjectMapper();

        return (List<ReviewListItem>) objectMapper.readValue(body.toString(), new TypeReference<List<ReviewListItem>>() {});
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

