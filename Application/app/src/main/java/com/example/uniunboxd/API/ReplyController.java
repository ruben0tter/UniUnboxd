package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.ReplyModel;
import com.example.uniunboxd.models.review.Reply;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * The ReplyController class is responsible for handling operations related to replies.
 * It provides a method to post a reply to a review.
 */
public class ReplyController {
    /**
     * Default constructor.
     */
    public ReplyController() {
    }

    /**
     * Posts a reply to a review.
     * It sends a POST request to the "Reply" endpoint with the reply text and the review ID.
     * If the response code is not 200, it throws an exception with the error message from the server.
     * @param model The ReplyModel containing the reply text and the review ID.
     * @param f The FragmentActivity from which this method is called.
     * @return The Reply object representing the posted reply.
     * @throws Exception If the response code is not 200.
     */
    public static Reply postReply(ReplyModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("text", model.text);
        json.put("reviewId", model.reviewId);

        HttpURLConnection con = APIClient.post("Reply", json.toString(), JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<Reply>() {
        });
    }
}
