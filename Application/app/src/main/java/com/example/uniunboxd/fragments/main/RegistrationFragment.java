package com.example.uniunboxd.fragments.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.RegistrationController;
import com.example.uniunboxd.DTO.RegisterModel;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;

import java.util.ArrayList;
import java.util.List;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private Spinner userType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        // Inputs
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        repeatPassword = view.findViewById(R.id.repeatPassword);
        userType = view.findViewById(R.id.userType);
        fillDropDown();

        // Buttons
        Button signUp = view.findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        Button signIn = view.findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signUp) {
            try {
                signUp();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            redirectToSignIn();
        }
    }

    private void fillDropDown() {
        List<String> types = new ArrayList<>();
        types.add("Student");
        types.add("University");
        types.add("Professor");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userType.setAdapter(adapter);
    }

    private void signUp() throws Exception {
        if (!arePasswordsEqual()) {
            sendNotification("Passwords are not equal.");
            return;
        }

        RegisterModel model = createRegisterModel();

        AsyncTask.execute(() -> {
            try {
                RegistrationController.register(model);
                redirectToSignIn();
            } catch (Exception e) {
                Log.e("APP", "Failed to register user: " + e);
            }
        });
    }

    private void redirectToSignIn() {
        ((IActivity) getActivity()).replaceFragment(new AuthenticationFragment(), false);
    }

    private boolean arePasswordsEqual() {
        return password.getText().toString().equals(repeatPassword.getText().toString());
    }

    private RegisterModel createRegisterModel() {
        return new RegisterModel(
                email.getText().toString(),
                password.getText().toString(),
                userType.getSelectedItem().toString());
    }

    private void sendNotification(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}