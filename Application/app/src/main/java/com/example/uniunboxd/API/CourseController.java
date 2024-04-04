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

public class CourseController {
    public CourseController() {
    }

    public static CourseRetrievalModel getCourseById(int id, int numOfReviews, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course?id=" + id + "&numReviews=" + numOfReviews, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<CourseRetrievalModel>() {
        });
    }

    public static List<PopularCourse> getPopularCourses(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/popular", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<PopularCourse>>() {
        });
    }

    public static List<PopularCourse> getPopularCoursesByUniversity(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/popular-by-university?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<PopularCourse>>() {
        });
    }

    public static List<PopularCourse> getPopularCoursesByFriends(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/popular-by-friends", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<PopularCourse>>() {
        });
    }

    public static List<OverviewCourse> getLastEditedCoursesByUniversity(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course/last-edited", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<OverviewCourse>>() {
        });
    }

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

    public static void deleteCourse(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.delete("Course?id=" + id, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }
}
