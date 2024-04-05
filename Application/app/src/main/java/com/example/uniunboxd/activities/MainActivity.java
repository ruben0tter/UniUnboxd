package com.example.uniunboxd.activities;

import android.os.Bundle;

import com.example.uniunboxd.databinding.ActivityMainBinding;
import com.example.uniunboxd.fragments.main.AuthenticationFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.StackHandler;

import java.util.Objects;

/**
 * MainActivity is the entry point of the application.
 * It extends IActivity, which provides methods for handling back press, replacing activities and fragments, and saving instance state.
 * It uses ActivityMainBinding for view binding, which replaces findViewById calls.
 */
public class MainActivity extends IActivity {
    // Binding instance for the activity's view
    ActivityMainBinding binding;

    /**
     * This method is called when the activity is starting.
     * It inflates the activity's layout, sets the content view, and sets up the initial fragment.
     * If there are fragments in the fragment history, it goes back to the previous fragment.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Set the inflated layout as the content view
        setContentView(binding.getRoot());

        // Get the StackHandler instance
        StackHandler stackHandler = StackHandler.getInstance();
        // If there are fragments in the fragment history, go back to the previous fragment
        if (stackHandler.stack != null && !stackHandler.empty()) {
            fragmentHistory = stackHandler.stack;
            goBack();
        } else {
            // Otherwise, set up the initial fragment
            setupInitialFragment();
        }
    }

    /**
     * This method sets up the initial fragment based on the user's login status and type.
     * If the user is logged in, it replaces the activity with the appropriate activity based on the user's type.
     * If the user is not logged in, it replaces the fragment with the AuthenticationFragment.
     */
    private void setupInitialFragment() {
        // If the user is logged in
        if (JWTValidation.isUserLoggedIn(this)) {
            // Get the user's type from the JWT token
            String type = JWTValidation.getTokenProperty(this, "typ");

            // Replace the activity with the appropriate activity based on the user's type
            if (Objects.equals(type, "Student")) {
                replaceActivity(StudentActivity.class);
            } else if (Objects.equals(type, "University")) {
                replaceActivity(UniversityActivity.class);
            } else if (Objects.equals(type, "Professor")) {
                replaceActivity(ProfessorActivity.class);
            }
        } else {
            // If the user is not logged in, replace the fragment with the AuthenticationFragment
            replaceFragment(new AuthenticationFragment(), false);
        }
    }
}
