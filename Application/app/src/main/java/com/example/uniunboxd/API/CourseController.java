package com.example.uniunboxd.API;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.models.CourseCreationModel;
import com.example.uniunboxd.models.CourseRetrievalModel;
import com.example.uniunboxd.models.home.PopularCourse;
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

public class CourseController {
    public static CourseRetrievalModel getCourseById(int id, FragmentActivity f) throws IOException {
        HttpURLConnection con = APIClient.get("Course?id=" + id, JWTValidation.getToken(f));

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

        return objectMapper.readValue(body.toString(), CourseRetrievalModel.class);
    }

    public static List<PopularCourse> getPopularCourses(FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("Course/popular", JWTValidation.getToken(f));
        StringBuilder body = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        if (con.getResponseCode() == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("Its over", "Once again");
            }
        } else {
            Log.d("It's over", "we are not making it out");
        }

        return objectMapper.readValue(body.toString(), new TypeReference<List<PopularCourse>>(){});
    }

    public static List<PopularCourse> getPopularCoursesByUniversity(int id, FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("Course/popular-by-university?id=" + id, JWTValidation.getToken(f));
        StringBuilder body = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        if (con.getResponseCode() == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("Its over", "Once again");
            }
        } else {
            Log.d("It's over", "we are not making it out");
        }

        return objectMapper.readValue(body.toString(), new TypeReference<List<PopularCourse>>(){});
    }

    public static List<PopularCourse> getPopularCoursesByFriends(FragmentActivity f) throws IOException{
        HttpURLConnection con = APIClient.get("Course/popular-by-friends", JWTValidation.getToken(f));
        StringBuilder body = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        if (con.getResponseCode() == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    body.append(responseLine);
                }
            } catch(Exception e) {
                Log.e("Its over", "Once again");
            }
        } else {
            Log.d("It's over", "we are not making it out");
        }

        return objectMapper.readValue(body.toString(), new TypeReference<List<PopularCourse>>(){});
    }

    public static HttpURLConnection postCourse(CourseCreationModel model, FragmentActivity f) throws Exception{
        JSONObject json = new JSONObject();
        json.put("name", model.Name);
        json.put("code", model.Code);
        json.put("description", model.Description);
        json.put("professor", model.Professor);
        json.put("image", model.Image);
        json.put("banner", model.Banner);
        json.put("universityId", model.UniversityID);

        return APIClient.post("Course", json.toString(), JWTValidation.getToken(f));
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
