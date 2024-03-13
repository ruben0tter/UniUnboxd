package com.example.uniunboxd.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.databinding.ActivityMainBinding;
import com.example.uniunboxd.fragments.main.AuthenticationFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.Redirection;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                // TODO: Add Professor Activity
            }
        } else {
            replaceFragment(new AuthenticationFragment());
        }
    }

    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Redirection.replaceActivity(this, activity);
    }

    public void replaceFragment(Fragment fragment) {
        Redirection.replaceFragment(this, fragment);
    }
}
