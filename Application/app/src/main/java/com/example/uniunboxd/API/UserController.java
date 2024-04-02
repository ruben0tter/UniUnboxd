package com.example.uniunboxd.API;

import android.util.Log;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserController {
    public static HttpURLConnection setDeviceToken(String deviceToken, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("deviceToken", deviceToken);

        return APIClient.put("User/set-device-token", json.toString(), JWTValidation.getToken(f));
    }

    public static HttpURLConnection follow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/follow?id=" + id, null, JWTValidation.getToken(f));
    }

    public static HttpURLConnection unfollow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/unfollow?id=" + id, null, JWTValidation.getToken(f));
    }

    public static List<AssignedProfessorModel> getAssignedProfessors(int id, FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("User/get-assigned-professors?id=" + id, JWTValidation.getToken(f));
        Log.i("APP", "Code: " + con.getResponseCode());

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("ERR", e.toString());
            }
        } else {
            String test = readMessage(con.getErrorStream());
            Log.d("PLS", test);
        }

        Log.d("APP1", body.toString());
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(body.toString(), new TypeReference<List<AssignedProfessorModel>>(){});
    }

    public static AssignedProfessorModel getAssignedProfessorByEmail(String email, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/get-assigned-professor?email="+email, JWTValidation.getToken(f));
        Log.i("APP", "Code:" + con.getResponseCode());
        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("ERR", e.toString());
            }
        } else {
            String test = readMessage(con.getErrorStream());
            Log.d("PLS", test);
            return null;
        }

        Log.d("APP1", body.toString());
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(body.toString(), AssignedProfessorModel.class);

    }

    public static ProfessorProfileModel getProfessorProfile(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("User/professor?id=" + id, JWTValidation.getToken(f));

        Log.i("APP", "Code: " + con.getResponseCode());

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("ERR", e.toString());
            }
        } else {
            String test = readMessage(con.getErrorStream());
            Log.d("PLS", test);
        }

        Log.d("APP1", body.toString());
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(body.toString(), ProfessorProfileModel.class);
    }

    public static StudentProfileModel getStudent(int id, FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("User/student?id=" + id, JWTValidation.getToken(f));

        Log.i("APP", "Code: " + con.getResponseCode());

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("ERR", e.toString());
            }
        } else {
            String test = readMessage(con.getErrorStream());
            Log.d("PLS", test);
        }

        Log.d("APP1", body.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        Log.i("Student", body.toString());
        return objectMapper.readValue(body.toString(), StudentProfileModel.class);
    }


    public static StudentListItem getStudentListItem(int userId, FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("User/student-list-item?id=" + userId, JWTValidation.getToken(f));

        Log.i("APP", "Code: " + con.getResponseCode());

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("ERR", e.toString());
            }
        } else {
            String test = readMessage(con.getErrorStream());
            Log.d("PLS", test);
        }

        Log.d("APP1", body.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        Log.i("Student", body.toString());
        return objectMapper.readValue(body.toString(), StudentListItem.class);
    }

    public static List<UniversityNameModel> GetUniversities(FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("User/get-universities", JWTValidation.getToken(f));

        Log.i("APP", "Code: " + con.getResponseCode());

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("ERR", e.toString());
            }
        } else {
            String test = readMessage(con.getErrorStream());
            Log.d("PLS", test);
        }

        Log.d("APP1", body.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        Log.i("Student", body.toString());
        return objectMapper.readValue(body.toString(), new TypeReference<List<UniversityNameModel>>(){});
    }

    public static HttpURLConnection putStudent(StudentEditModel model, FragmentActivity f) throws Exception {
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

        return APIClient.put("User/student", json.toString(), JWTValidation.getToken(f));
    }
    public static HttpURLConnection putProfessor(ProfessorEditModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", model.Id);
        json.put("image", model.Image);
        json.put("name", model.Name);
        return APIClient.put("User/professor", json.toString(), JWTValidation.getToken(f));
    }

    private static String readMessage(InputStream content) throws IOException{
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (content, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        return textBuilder.toString().replace("\"", "");
    }

}
