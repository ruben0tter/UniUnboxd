package com.example.uniunboxd.API;

import android.content.Context;
import android.util.Log;

import com.example.uniunboxd.models.CourseSearchResult;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class SearchController {
    public static List<CourseSearchResult> searchCourses(String search, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Course/get?search=" + search, JWTValidation.getToken(c));

        if (con.getResponseCode() == 200) {
            String body = APIClient.readStream(con.getInputStream());

            Log.d("APP1", body);
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body, new TypeReference<List<CourseSearchResult>>() {
            });
        } else {
            String test = APIClient.readStream(con.getErrorStream());
            Log.d("PLS", test);
            throw new IOException("Failed to search courses");
        }
    }
}
