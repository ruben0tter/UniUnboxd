package com.example.uniunboxd.API;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.models.professor.ProfessorEditModel;
import com.example.uniunboxd.models.professor.ProfessorProfileModel;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.models.student.StudentProfileModel;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class UserController {
    public static HttpURLConnection follow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/follow?id=" + id, null, JWTValidation.getToken(f));
    }

    public static HttpURLConnection unfollow(int id, FragmentActivity f) throws Exception {
        return APIClient.put("User/unfollow?id=" + id, null, JWTValidation.getToken(f));
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

    public static HttpURLConnection putStudent(StudentEditModel model, FragmentActivity f) throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", model.Id);
        json.put("image", model.Image);
        json.put("name", model.Name);
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
