package com.example.uniunboxd.API;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.FlagReviewModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.models.home.FriendReview;
import com.example.uniunboxd.models.ReviewListItem;
import com.example.uniunboxd.models.review.Review;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    public static Review getReview(int id, FragmentActivity f) throws Exception{
        HttpURLConnection con = APIClient.get("Review?id=" + id, JWTValidation.getToken(f));

        if (con.getResponseCode() == 200) {
            StringBuilder body = new StringBuilder();

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;

                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }

                return new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                        .readValue(body.toString(), Review.class);
            } catch(Exception e) {
                Log.e("API", "Exception: " + e);

                return null;
            }
        } else {
            String error = readMessage(con.getErrorStream());
            Log.e("API", "Code: " + con.getResponseCode());
            Log.e("API", "Error: " + error);

            return null;
        }
    }

    public static List<FriendReview> getLatestReviewsByFriends(FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("Review/latest-by-friends", JWTValidation.getToken(f));
        StringBuilder body = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        if (con.getResponseCode() == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("Its over", "Once again");
            }
        } else {
            Log.d("It's over", "we are not making it out");
        }

        return objectMapper.readValue(body.toString(), new TypeReference<List<FriendReview>>(){});
    }

    public static HttpURLConnection postReview(ReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("rating", model.rating);
        json.put("comment", model.comment);
        json.put("isAnonymous", model.isAnonymous);
        json.put("courseId", model.courseId);

        return APIClient.post("Review", json.toString(), JWTValidation.getToken(f));
    }

    public static List<ReviewListItem> getReviewListItems(int id, int courseId, int num, FragmentActivity f) throws Exception {
        HttpURLConnection con =  APIClient.get("Review/get-next-reviews?id="+id + "&courseId="+courseId +"&numReviews="+num, JWTValidation.getToken(f));

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

    public static HttpURLConnection putReview(ReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", model.id);
        json.put("rating", model.rating);
        json.put("comment", model.comment);
        json.put("isAnonymous", model.isAnonymous);
        json.put("courseId", model.courseId);

        return APIClient.put("Review?id=" + model.id, json.toString(), JWTValidation.getToken(f));
    }

    public static HttpURLConnection like(int id, FragmentActivity f) throws Exception {
        return APIClient.put("Review/like?review=" + id, null, JWTValidation.getToken(f));
    }

    public static HttpURLConnection unlike(int id, FragmentActivity f) throws Exception {
        return APIClient.put("Review/unlike?review=" + id, null, JWTValidation.getToken(f));
    }

    public static HttpURLConnection flagReview(FlagReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("reviewId", model.reviewId);
        json.put("message", model.message);

        return APIClient.post("Review/flag-review", json.toString(), JWTValidation.getToken(f));
    }

    public static HttpURLConnection deleteReview(int id, FragmentActivity f) throws Exception {
        return APIClient.delete("Review?id=" + id, JWTValidation.getToken(f));
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

