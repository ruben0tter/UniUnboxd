package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.ReplyModel;
import com.example.uniunboxd.models.review.Reply;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class ReplyController {
    public ReplyController() {
    }

    public static Reply postReply(ReplyModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("text", model.text);
        json.put("reviewId", model.reviewId);

        HttpURLConnection con = APIClient.post("Reply", json.toString(), JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<Reply>() {
        });
    }
}
