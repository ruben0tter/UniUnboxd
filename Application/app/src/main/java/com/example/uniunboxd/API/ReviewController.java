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

/**
 * The ReviewController class is responsible for handling operations related to reviews.
 * It provides methods to get a review by ID, get the latest reviews by friends, post a new review,
 * get a list of review items, edit an existing review, like a review, unlike a review, flag a review, and delete a review.
 */
public class ReviewController {
    /**
     * Default constructor.
     */
    public ReviewController() {
    }

    /**
     * Retrieves a review by its ID.
     * @param id The ID of the review.
     * @param f The FragmentActivity from which this method is called.
     * @return The Review object representing the review.
     * @throws Exception If an error occurs.
     */
    public static Review getReview(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.get("Review?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<Review>() {
        });
    }

    /**
     * Retrieves the latest reviews by friends.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of FriendReview objects representing the latest reviews by friends.
     * @throws IOException If an I/O error occurs.
     */
    public static List<FriendReview> getLatestReviewsByFriends(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Review/latest-by-friends", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<FriendReview>>() {
        });
    }

    /**
     * Posts a new review.
     * @param model The ReviewModel representing the new review.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void postReview(ReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("rating", model.rating);
        json.put("comment", model.comment);
        json.put("isAnonymous", model.isAnonymous);
        json.put("courseId", model.courseId);

        HttpURLConnection con = APIClient.post("Review", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Retrieves a list of review items.
     * @param id The ID of the review.
     * @param courseId The ID of the course.
     * @param num The number of reviews to retrieve.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of ReviewListItem objects representing the review items.
     * @throws Exception If an error occurs.
     */
    public static List<ReviewListItem> getReviewListItems(int id, int courseId, int num, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.get("Review/get-next-reviews?id=" + id + "&courseId=" + courseId + "&numReviews=" + num, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<ReviewListItem>>() {
        });
    }

    /**
     * Edits an existing review.
     * @param model The ReviewModel representing the review to edit.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
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

    /**
     * Likes a review.
     * @param id The ID of the review to like.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void like(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("Review/like?review=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Unlikes a review.
     * @param id The ID of the review to unlike.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void unlike(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("Review/unlike?review=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Flags a review.
     * @param model The FlagReviewModel representing the review to flag.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void flagReview(FlagReviewModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("reviewId", model.reviewId);
        json.put("message", model.message);

        HttpURLConnection con = APIClient.post("Review/flag-review", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Deletes a review.
     * @param id The ID of the review to delete.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void deleteReview(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.delete("Review?id=" + id, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }
}
