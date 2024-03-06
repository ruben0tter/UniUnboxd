package com.example.uniunboxd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.API.AuthenticationController;
import com.example.uniunboxd.DTO.AuthenticationModel;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class AuthenticationFragment extends Fragment implements View.OnClickListener {

    private EditText email;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_authentication, container, false);

        // Inputs
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);

        // Buttons
        Button signIn = (Button) view.findViewById(R.id.sign_in_button);
        signIn.setOnClickListener(this);

        Button signUp = (Button) view.findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            try {
                signIn();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            replaceFragment(new RegistrationFragment());
        }
    }

    private void signIn() throws Exception {
        AuthenticationModel model = createAuthenticationModel();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection response = AuthenticationController.authenticate(model);
                    //TODO: Fix notification system.
                    //sendNotification(readMessage(response.getErrorStream()));
                    if (response.getResponseCode() == 200) {
                        String json = readMessage(response.getInputStream());
                        String token = getToken(json);
                        placeToken(token);
                    }
                } catch (Exception e) {
                    Log.e("APP", "Failed to register user: " + e.toString());
                }

            }
        });
    }

    private AuthenticationModel createAuthenticationModel() {
        return new AuthenticationModel(
                email.getText().toString(),
                password.getText().toString());
    }

    private void placeToken(String token) throws NullPointerException {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs=getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.putString("token", token);
        edit.apply();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void sendNotification(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private String readMessage(InputStream content) throws IOException {
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

    private String getToken(String json) {
        json = json.substring(json.indexOf(":") + 1);
        return json.substring(0, json.indexOf("}"));
    }
}
