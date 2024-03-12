package com.example.uniunboxd.API;

import android.util.Log;

import com.example.uniunboxd.models.CourseCreationModel;
import com.example.uniunboxd.models.CourseRetrievalModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class CourseController {
    public static CourseRetrievalModel getCourseById(int id) throws IOException {
        HttpURLConnection con = APIClient.get("Course/get?id=" + id);

        Log.i("APP", "Code: " + con.getResponseCode());

        StringBuilder body = new StringBuilder();

        if (con.getResponseCode() == 200) {

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
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

    public static HttpURLConnection postCourse(CourseCreationModel model) throws Exception{
        JSONObject json = new JSONObject();
        json.put("name", model.Name);
        json.put("code", model.Code);
        json.put("description", model.Description);
        json.put("professor", model.Professor);
        json.put("image", model.Image);
        json.put("banner", model.Banner);
        json.put("universityId", model.UniversityID);

        return APIClient.post("Course/create", json.toString());
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