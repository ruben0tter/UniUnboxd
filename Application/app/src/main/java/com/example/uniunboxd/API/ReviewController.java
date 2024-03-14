package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.utilities.JWTValidation;

import org.json.JSONObject;

import java.net.HttpURLConnection;

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
}
