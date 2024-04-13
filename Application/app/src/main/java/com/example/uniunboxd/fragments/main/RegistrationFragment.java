package com.example.uniunboxd.fragments.main;

import android.os.AsyncTask;
import android.os.Bundle;
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

/**
 * RegistrationFragment class that represents the registration screen.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private Spinner userType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the registration fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the registration fragment.
     */
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

    /**
     * Handles the click event.
     *
     * @param view The view.
     */
    @Override
    public void onClick(View view) {
        // Sign up.
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

    // Fill the drop down with user types.
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

    // Sign up the user.
    private void signUp() {
        // Check if the passwords are equal.
        if (!arePasswordsEqual()) {
            sendNotification("Passwords are not equal.");
            return;
        }

        RegisterModel model = createRegisterModel();

        // Register the user.
        AsyncTask.execute(() -> {
            try {
                // Register the user with the API.
                RegistrationController.register(model);
                // Redirect to the sign in screen.
                redirectToSignIn();
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), e.getMessage().replace("\"", ""), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void redirectToSignIn() {
        // Redirect to the sign in screen by replacing the current fragment.
        ((IActivity) getActivity()).replaceFragment(new AuthenticationFragment(), false);
    }

    // Check if the passwords are equal.
    private boolean arePasswordsEqual() {
        return password.getText().toString().equals(repeatPassword.getText().toString());
    }

    // Create the register model.
    private RegisterModel createRegisterModel() {
        return new RegisterModel(
                email.getText().toString(),
                password.getText().toString(),
                userType.getSelectedItem().toString());
    }

    // Send a notification.
    private void sendNotification(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}