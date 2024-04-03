package com.example.uniunboxd.API;

import android.content.Context;
import android.util.Log;

import com.example.uniunboxd.models.CourseSearchResult;
import com.example.uniunboxd.models.UserSearchResult;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class SearchController {
    public static int SEARCH_LIMIT = 10;

    public static List<CourseSearchResult> searchCourses(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/Course?Count=" + SEARCH_LIMIT + "&Search=" + text, JWTValidation.getToken(c));

        if (con.getResponseCode() == 200) {
            String body = APIClient.readStream(con.getInputStream());

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body, new TypeReference<List<CourseSearchResult>>() {
            });
        } else {

            String test = APIClient.readStream(con.getErrorStream());
            Log.d("PLS", test);
            Log.d("PLS-code", "" + con.getResponseCode());

            throw new IOException("Failed to search courses");
        }
    }

    public static List<UserSearchResult> searchUsers(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/User?Count=" + SEARCH_LIMIT + "&Search=" + text, JWTValidation.getToken(c));

        if (con.getResponseCode() == 200) {
            String body = APIClient.readStream(con.getInputStream());

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body, new TypeReference<List<UserSearchResult>>() {
            });
        } else {
            String test = APIClient.readStream(con.getErrorStream());
            Log.d("PLS", test);
            throw new IOException("Failed to search users");
        }
    }

    public static List<CourseSearchResult> searchUniversity(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/Course?Count=" + SEARCH_LIMIT + "&UniverityId=" +
                JWTValidation.getTokenProperty(c, "sub") + "&Search=" + text, JWTValidation.getToken(c));

        if (con.getResponseCode() == 200) {
            String body = APIClient.readStream(con.getInputStream());

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body, new TypeReference<List<CourseSearchResult>>() {
            });
        } else {

            String test = APIClient.readStream(con.getErrorStream());
            Log.d("PLS", test);
            Log.d("PLS-code", "" + con.getResponseCode());

            throw new IOException("Failed to search courses");
        }
    }
}
