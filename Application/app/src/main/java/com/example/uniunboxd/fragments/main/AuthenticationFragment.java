package com.example.uniunboxd.fragments.main;

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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.API.AuthenticationController;
import com.example.uniunboxd.DTO.AuthenticationModel;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.StudentActivity;
import com.example.uniunboxd.activities.UniversityActivity;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
                    if (response.getResponseCode() == 200) {
                        String json = readMessage(response.getInputStream());
                        String token = getToken(json);
                        placeToken(token);
                        Log.i("JWT", token);
                        redirectToHomePage();
                    } else {
                        MessageHandler.showToastFromBackground(getActivity(), response.getErrorStream());
                    }
                } catch (Exception e) {
                    Log.e("APP", "Failed to authenticate user: " + e.toString());
                }

            }
        });
    }

    private AuthenticationModel createAuthenticationModel() {
        return new AuthenticationModel(
                email.getText().toString(),
                password.getText().toString());
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

    private void placeToken(String token) throws NullPointerException {
        SharedPreferences prefs = getActivity()
                .getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("token", token);
        edit.apply();
    }

    private void redirectToHomePage() {
        String userType = JWTValidation.getTokenProperty(getActivity(),"typ");

        if (Objects.equals(userType, "Student")) {
            ((IActivity) getActivity()).replaceActivity(StudentActivity.class);
        } else if (Objects.equals(userType, "University")) {
            ((IActivity) getActivity()).replaceActivity(UniversityActivity.class);
        } else if (Objects.equals(userType, "Professor")) {
            //TODO: Redirect to Professor Home Page.
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
