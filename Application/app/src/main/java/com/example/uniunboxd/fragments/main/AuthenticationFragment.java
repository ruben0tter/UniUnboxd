package com.example.uniunboxd.fragments.main;

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
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.ProfessorActivity;
import com.example.uniunboxd.activities.StudentActivity;
import com.example.uniunboxd.activities.UniversityActivity;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.Objects;

/**
 * AuthenticationFragment class that represents the authentication screen.
 */
public class AuthenticationFragment extends Fragment implements View.OnClickListener {
    private EditText email;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the authentication fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the authentication fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);

        // Inputs
        email = view.findViewById(R.id.email_input);
        password = view.findViewById(R.id.password_input);

        // Sign In Button
        Button signIn = view.findViewById(R.id.sign_in_button);
        signIn.setOnClickListener(this);

        // Sign Up Button
        Button signUp = view.findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(this);

        return view;
    }

    /**
     * Handles the click event for the sign in and sign up buttons.
     *
     * @param view The view.
     */
    @Override
    public void onClick(View view) {
        // Sign in.
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

    // Sign in the user.
    private void signIn() {
        AuthenticationModel model = createAuthenticationModel();

        // Authenticate the user.
        AsyncTask.execute(() -> {
            try {
                // Get the token from the API.
                String token = AuthenticationController.authenticate(model);
                
                // Place the token in the shared preferences.
                JWTValidation.placeToken(token, getActivity());
                Log.i("JWT", token);
                redirectToHomePage();
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), e.getMessage().replace("\"", ""), Toast.LENGTH_LONG).show());
            }
        });
    }

    // Create the authentication model.
    private AuthenticationModel createAuthenticationModel() {
        return new AuthenticationModel(
                email.getText().toString(),
                password.getText().toString());
    }

    // Redirect to the home page.
    private void redirectToHomePage() {
        String userType = JWTValidation.getTokenProperty(getActivity(), "typ");

        // Redirect to the appropriate home page.
        if (Objects.equals(userType, "Student")) {
            ((IActivity) getActivity()).replaceActivity(StudentActivity.class);
        } else if (Objects.equals(userType, "University")) {
            ((IActivity) getActivity()).replaceActivity(UniversityActivity.class);
        } else if (Objects.equals(userType, "Professor")) {
            ((IActivity) getActivity()).replaceActivity(ProfessorActivity.class);
        }
    }

    // Replace the fragment.
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
