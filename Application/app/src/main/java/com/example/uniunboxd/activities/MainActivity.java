package com.example.uniunboxd.activities;

import android.os.Bundle;

import com.example.uniunboxd.databinding.ActivityMainBinding;
import com.example.uniunboxd.fragments.main.AuthenticationFragment;
import com.example.uniunboxd.fragments.student.StudentEditFragment;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.models.student.StudentProfileModel;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.Objects;

public class MainActivity extends IActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        replaceFragment(new StudentEditFragment(new StudentEditModel(new StudentProfileModel(3, "Name", "Uni", "string", null, null, null))));
        setupInitialFragment();
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
