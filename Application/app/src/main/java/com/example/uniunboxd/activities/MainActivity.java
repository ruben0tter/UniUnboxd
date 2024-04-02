package com.example.uniunboxd.activities;

import android.os.Bundle;

import com.example.uniunboxd.databinding.ActivityMainBinding;
import com.example.uniunboxd.fragments.main.AuthenticationFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.StackHandler;

import java.util.Objects;

public class MainActivity extends IActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StackHandler stackHandler = StackHandler.getInstance();
        if(stackHandler.stack != null) {
            fragmentHistory = stackHandler.stack;
            goBack();
        } else {
            setupInitialFragment();
        }
    }

    private void setupInitialFragment() {
        if (JWTValidation.isUserLoggedIn(this)) {
            String type = JWTValidation.getTokenProperty(this, "typ");

            if (Objects.equals(type, "Student")) {
                replaceActivity(StudentActivity.class);
            } else if (Objects.equals(type, "University")) {
                replaceActivity(UniversityActivity.class);
            } else if (Objects.equals(type, "Professor")) {
                replaceActivity(ProfessorActivity.class);
            }
        } else {
            replaceFragment(new AuthenticationFragment(), false);
        }
    }
}
