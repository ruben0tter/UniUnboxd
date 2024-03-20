package com.example.uniunboxd.API;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.ReplyModel;
import com.example.uniunboxd.models.review.Reply;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class ReplyController {
    public static Reply postReply(ReplyModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("text", model.text);
        json.put("reviewId", model.reviewId);

        HttpURLConnection con = APIClient.post("Reply", json.toString(), JWTValidation.getToken(f));

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;

                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }

                return new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                        .readValue(body.toString(), Reply.class);
            } catch(Exception e) {
                Log.e("API", "Exception: " + e);

                return null;
            }
        } else {
            Log.e("API", "Code: " + con.getResponseCode());

            return null;
        }
    }
}
