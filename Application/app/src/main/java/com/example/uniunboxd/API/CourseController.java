package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.models.course.AssignedProfessorModel;
import com.example.uniunboxd.models.course.CourseCreationModel;
import com.example.uniunboxd.models.course.CourseEditModel;
import com.example.uniunboxd.models.course.CourseRetrievalModel;
import com.example.uniunboxd.models.home.OverviewCourse;
import com.example.uniunboxd.models.home.PopularCourse;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * The CourseController class is responsible for handling operations related to courses.
 * It provides methods to get a course by ID, get popular courses, get popular courses by university, get popular courses by friends,
 * get last edited courses by university, post a new course, edit an existing course, and delete a course.
 */
public class CourseController {
    /**
     * Default constructor.
     */
    public CourseController() {
    }

    /**
     * Retrieves a course by its ID.
     * @param id The ID of the course.
     * @param numOfReviews The number of reviews to retrieve.
     * @param f The FragmentActivity from which this method is called.
     * @return The CourseRetrievalModel representing the course.
     * @throws IOException If an I/O error occurs.
     */
    public static CourseRetrievalModel getCourseById(int id, int numOfReviews, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course?id=" + id + "&numReviews=" + numOfReviews, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<CourseRetrievalModel>() {
        });
    }

    /**
     * Retrieves popular courses.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of PopularCourse objects representing the popular courses.
     * @throws IOException If an I/O error occurs.
     */
    public static List<PopularCourse> getPopularCourses(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/popular", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<PopularCourse>>() {
        });
    }

    /**
     * Retrieves popular courses by university.
     * @param id The ID of the university.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of PopularCourse objects representing the popular courses.
     * @throws IOException If an I/O error occurs.
     */
    public static List<PopularCourse> getPopularCoursesByUniversity(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/popular-by-university?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<PopularCourse>>() {
        });
    }

    /**
     * Retrieves popular courses by friends.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of PopularCourse objects representing the popular courses.
     * @throws IOException If an I/O error occurs.
     */
    public static List<PopularCourse> getPopularCoursesByFriends(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/popular-by-friends", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<PopularCourse>>() {
        });
    }

    /**
     * Retrieves the last edited courses by university.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of OverviewCourse objects representing the last edited courses.
     * @throws IOException If an I/O error occurs.
     */
    public static List<OverviewCourse> getLastEditedCoursesByUniversity(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/last-edited", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<OverviewCourse>>() {
        });
    }

    /**
     * Posts a new course.
     * @param model The CourseCreationModel representing the new course.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void postCourse(CourseCreationModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", model.Name);
        json.put("code", model.Code);
        json.put("description", model.Description);
        json.put("professor", model.Professor);
        json.put("image", model.Image);
        json.put("banner", model.Banner);
        json.put("universityId", model.UniversityID);
        JSONArray assignedProfessors = new JSONArray();
        for (AssignedProfessorModel x : model.AssignedProfessors) {
            JSONObject assignedProfessor = new JSONObject();
            assignedProfessor.put("id", x.Id);
            assignedProfessor.put("name", x.Name);
            assignedProfessor.put("email", x.Email);
            assignedProfessors.put(assignedProfessor);
        }
        json.put("assignedProfessors", assignedProfessors);
        HttpURLConnection con = APIClient.post("Course", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Edits an existing course.
     * @param course The CourseEditModel representing the course to edit.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void putCourse(CourseEditModel course, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", course.Id);
        json.put("name", course.Name);
        json.put("code", course.Code);
        json.put("description", course.Description);
        json.put("professor", course.Professor);
        json.put("image", course.Image);
        json.put("banner", course.Banner);
        JSONArray assignedProfessors = new JSONArray();
        for (AssignedProfessorModel x : course.AssignedProfessors) {
            JSONObject assignedProfessor = new JSONObject();
            assignedProfessor.put("id", x.Id);
            assignedProfessor.put("name", x.Name);
            assignedProfessor.put("email", x.Email);
            assignedProfessors.put(assignedProfessor);
        }
        json.put("assignedProfessors", assignedProfessors);
        HttpURLConnection con = APIClient.put("Course", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Deletes a course.
     * @param id The ID of the course to delete.
     * @param f The FragmentActivity from which this method is called.
     * @throws IOException If an I/O error occurs.
     */
    public static void deleteCourse(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.delete("Course?id=" + id, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }
}
