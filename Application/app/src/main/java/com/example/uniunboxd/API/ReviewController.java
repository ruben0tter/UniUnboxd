package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.FlagReviewModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.models.ReviewListItem;
import com.example.uniunboxd.models.home.FriendReview;
import com.example.uniunboxd.models.review.Review;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class ReviewController {
    public ReviewController() {
    }

    public static Review getReview(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.get("Review?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<Review>() {
        });
    }

    public static List<FriendReview> getLatestReviewsByFriends(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Review/latest-by-friends", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<FriendReview>>() {
        });
    }

    public static void postReview(ReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("rating", model.rating);
        json.put("comment", model.comment);
        json.put("isAnonymous", model.isAnonymous);
        json.put("courseId", model.courseId);

        HttpURLConnection con = APIClient.post("Review", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static List<ReviewListItem> getReviewListItems(int id, int courseId, int num, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.get("Review/get-next-reviews?id=" + id + "&courseId=" + courseId + "&numReviews=" + num, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<ReviewListItem>>() {
        });
    }

    public static void putReview(ReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", model.id);
        json.put("rating", model.rating);
        json.put("comment", model.comment);
        json.put("isAnonymous", model.isAnonymous);
        json.put("courseId", model.courseId);

        HttpURLConnection con = APIClient.put("Review?id=" + model.id, json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static void like(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("Review/like?review=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static void unlike(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("Review/unlike?review=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static void flagReview(FlagReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("reviewId", model.reviewId);
        json.put("message", model.message);

        HttpURLConnection con = APIClient.post("Review/flag-review", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static void deleteReview(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.delete("Review?id=" + id, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }
}
