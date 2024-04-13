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

/**
 * The UserController class is responsible for handling user-related operations.
 * It provides methods to set device token, follow/unfollow users, get assigned professors,
 * get professor or student profiles, get student list item, get universities, and update student or professor information.
 */
public class UserController {
    /**
     * Default constructor.
     */
    public UserController() {
    }

    /**
     * Sets the device token for the user.
     * @param deviceToken The device token to set.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void setDeviceToken(String deviceToken, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("deviceToken", deviceToken);

        HttpURLConnection con = APIClient.put("User/set-device-token", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Follows a user.
     * @param id The id of the user to follow.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void follow(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("User/follow?id=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Unfollows a user.
     * @param id The id of the user to unfollow.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void unfollow(int id, FragmentActivity f) throws Exception {
        HttpURLConnection con = APIClient.put("User/unfollow?id=" + id, null, JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }

    /**
     * Gets the professors assigned to a user.
     * @param id The id of the user.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of AssignedProfessorModel objects representing the assigned professors.
     * @throws IOException If an I/O error occurs.
     */
    public static List<AssignedProfessorModel> getAssignedProfessors(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/get-assigned-professors?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<AssignedProfessorModel>>() {
        });
    }

    /**
     * Gets the assigned professor by their email.
     * @param email The email of the professor.
     * @param f The FragmentActivity from which this method is called.
     * @return An AssignedProfessorModel object representing the assigned professor.
     * @throws IOException If an I/O error occurs.
     */
    public static AssignedProfessorModel getAssignedProfessorByEmail(String email, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/get-assigned-professor?email=" + email, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<AssignedProfessorModel>() {
        });
    }

    /**
     * Gets the profile of a professor by their id.
     * @param id The id of the professor.
     * @param f The FragmentActivity from which this method is called.
     * @return A ProfessorProfileModel object representing the professor's profile.
     * @throws IOException If an I/O error occurs.
     */
    public static ProfessorProfileModel getProfessorProfile(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/professor?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<ProfessorProfileModel>() {
        });
    }

    /**
     * Gets a student by their id.
     * @param id The id of the student.
     * @param f The FragmentActivity from which this method is called.
     * @return A StudentProfileModel object representing the student.
     * @throws IOException If an I/O error occurs.
     */
    public static StudentProfileModel getStudent(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/student?id=" + id, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<StudentProfileModel>() {
        });
    }

    /**
     * Gets a student list item by the user's id.
     * @param userId The id of the user.
     * @param f The FragmentActivity from which this method is called.
     * @return A StudentListItem object representing the student list item.
     * @throws IOException If an I/O error occurs.
     */
    public static StudentListItem getStudentListItem(int userId, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/student-list-item?id=" + userId, JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<StudentListItem>() {
        });
    }

    /**
     * Gets a list of universities.
     * @param f The FragmentActivity from which this method is called.
     * @return A list of UniversityNameModel objects representing the universities.
     * @throws IOException If an I/O error occurs.
     */
    public static List<UniversityNameModel> GetUniversities(FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/get-universities", JWTValidation.getToken(f));
        return APIClient.processResponse(con, new TypeReference<List<UniversityNameModel>>() {
        });
    }

    /**
     * Updates a student's information.
     * @param model The StudentEditModel object containing the student's updated information.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
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

    /**
     * Updates a professor's information.
     * @param model The ProfessorEditModel object containing the professor's updated information.
     * @param f The FragmentActivity from which this method is called.
     * @throws Exception If an error occurs.
     */
    public static void putProfessor(ProfessorEditModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", model.Id);
        json.put("image", model.Image);
        json.put("name", model.Name);
        HttpURLConnection con = APIClient.put("User/professor", json.toString(), JWTValidation.getToken(f));
        APIClient.processResponse(con, null);
    }
}
