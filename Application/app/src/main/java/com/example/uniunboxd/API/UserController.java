package com.example.uniunboxd.API;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.models.course.AssignedProfessorModel;
import com.example.uniunboxd.models.professor.ProfessorEditModel;
import com.example.uniunboxd.models.professor.ProfessorProfileModel;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.models.student.StudentListItem;
import com.example.uniunboxd.models.student.StudentProfileModel;
import com.example.uniunboxd.models.student.UniversityNameModel;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class UserController {
    public UserController() {
    }

    public static void setDeviceToken(String deviceToken, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("deviceToken", deviceToken);

        HttpURLConnection con = APIClient.put("User/set-device-token", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static void follow(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("User/follow?id=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static void unfollow(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("User/unfollow?id=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static List<AssignedProfessorModel> getAssignedProfessors(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/get-assigned-professors?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<AssignedProfessorModel>>() {
        });
    }

    public static AssignedProfessorModel getAssignedProfessorByEmail(String email, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/get-assigned-professor?email=" + email, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<AssignedProfessorModel>() {
        });
    }

    public static ProfessorProfileModel getProfessorProfile(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/professor?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<ProfessorProfileModel>() {
        });
    }

    public static StudentProfileModel getStudent(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/student?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<StudentProfileModel>() {
        });
    }


    public static StudentListItem getStudentListItem(int userId, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/student-list-item?id=" + userId, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<StudentListItem>() {
        });
    }

    public static List<UniversityNameModel> GetUniversities(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/get-universities", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<UniversityNameModel>>() {
        });
    }

    public static void putStudent(StudentEditModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", model.Id);
        json.put("image", model.Image);
        json.put("name", model.Name);

        JSONObject settings = new JSONObject();
        settings.put("studentId", model.NotificationSettings.StudentId);
        settings.put("receivesFollowersReviewMail", model.NotificationSettings.ReceivesFollowersReviewMail);
        settings.put("receivesFollowersReviewPush", model.NotificationSettings.ReceivesFollowersReviewPush);
        settings.put("receivesNewReplyMail", model.NotificationSettings.ReceivesNewReplyMail);
        settings.put("receivesNewReplyPush", model.NotificationSettings.ReceivesNewReplyPush);
        settings.put("receivesNewFollowerMail", model.NotificationSettings.ReceivesNewFollowerMail);
        settings.put("receivesNewFollowerPush", model.NotificationSettings.ReceivesNewFollowerPush);

        json.put("notificationSettings", settings);
        json.put("verificationStatus", model.VerificationStatus);

        HttpURLConnection con =  APIClient.put("User/student", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    public static void putProfessor(ProfessorEditModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", model.Id);
        json.put("image", model.Image);
        json.put("name", model.Name);
        HttpURLConnection con = APIClient.put("User/professor", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }
}
