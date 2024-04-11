package com.example.uniunboxd.API;

import android.content.Context;

import com.example.uniunboxd.models.CourseSearchResult;
import com.example.uniunboxd.models.UserSearchResult;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * The SearchController class is responsible for handling search operations.
 * It provides methods to search for courses, users, and universities.
 */
public class SearchController {
    /**
     * Default constructor.
     */
    public SearchController() {
    }

    /**
     * The maximum number of search results to return.
     */
    public static int SEARCH_LIMIT = 10;

    /**
     * Searches for courses with the given text.
     * @param text The text to search for.
     * @param c The context from which this method is called.
     * @return A list of CourseSearchResult objects representing the search results.
     * @throws IOException If an I/O error occurs.
     */
    public static List<CourseSearchResult> searchCourses(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/Course?Count=" + SEARCH_LIMIT + "&Search=" + text, JWTValidation.getToken(c));
        return APIClient.processResponse(con, new TypeReference<List<CourseSearchResult>>() {
        });
    }

    /**
     * Searches for users with the given text.
     * @param text The text to search for.
     * @param c The context from which this method is called.
     * @return A list of UserSearchResult objects representing the search results.
     * @throws IOException If an I/O error occurs.
     */
    public static List<UserSearchResult> searchUsers(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/User?Count=" + SEARCH_LIMIT + "&Search=" + text, JWTValidation.getToken(c));
        return APIClient.processResponse(con, new TypeReference<List<UserSearchResult>>() {
        });
    }

    /**
     * Searches for universities with the given text.
     * @param text The text to search for.
     * @param c The context from which this method is called.
     * @return A list of CourseSearchResult objects representing the search results.
     * @throws IOException If an I/O error occurs.
     */
    public static List<CourseSearchResult> searchUniversity(String text, Context c) throws IOException {
        HttpURLConnection con = APIClient.get("Search/Course?Count=" + SEARCH_LIMIT + "&UniverityId=" +
                JWTValidation.getTokenProperty(c, "sub") + "&Search=" + text, JWTValidation.getToken(c));
        return APIClient.processResponse(con, new TypeReference<List<CourseSearchResult>>() {
        });
    }
}
