package com.example.uniunboxd.API;

import android.content.Context;

import com.example.uniunboxd.models.CourseSearchResult;
import com.example.uniunboxd.models.UserSearchResult;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class SearchController {
    public SearchController() {

    }

    public static int SEARCH_LIMIT = 10;

    public static List<CourseSearchResult> searchCourses(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/Course?Count=" + SEARCH_LIMIT + "&Search=" + text, JWTValidation.getToken(c));
        return APIClient.processResponse(con, new TypeReference<List<CourseSearchResult>>() {
        });
    }

    public static List<UserSearchResult> searchUsers(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/User?Count=" + SEARCH_LIMIT + "&Search=" + text, JWTValidation.getToken(c));
        return APIClient.processResponse(con, new TypeReference<List<UserSearchResult>>() {
        });
    }

    public static List<CourseSearchResult> searchUniversity(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/Course?Count=" + SEARCH_LIMIT + "&UniverityId=" +
                JWTValidation.getTokenProperty(c, "sub") + "&Search=" + text, JWTValidation.getToken(c));
        return APIClient.processResponse(con, new TypeReference<List<CourseSearchResult>>() {
        });
    }
}
