package com.example.uniunboxd.API;

import com.example.uniunboxd.DTO.RegisterModel;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RegistrationController {
    public RegistrationController() {

    }

    public static void register(RegisterModel model) throws Exception {
        JSONObject json = new JSONObject();
        json.put("email", model.email);
        json.put("password", model.password);
        json.put("type", model.userType);

        HttpURLConnection con = APIClient.post("Registration", json.toString(), null);
        if(con.getResponseCode() != 200)
            throw new Exception(APIClient.readStream(con.getErrorStream()));
        APIClient.processResponse(con, null);
    }
}
