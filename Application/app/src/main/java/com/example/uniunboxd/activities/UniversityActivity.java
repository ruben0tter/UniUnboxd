package com.example.uniunboxd.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityUniversityBinding;
import com.example.uniunboxd.fragments.university.ApplicationsFragment;
import com.example.uniunboxd.fragments.university.HomeSubmittedFragment;
import com.example.uniunboxd.fragments.university.HomeUnverifiedFragment;
import com.example.uniunboxd.fragments.university.SearchUniversityFragment;
import com.example.uniunboxd.fragments.university.UniversityHomeFragment;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.Objects;
import java.util.Stack;

public class UniversityActivity extends IActivity {

    String status;
    Stack<Fragment> fragmentHistory = new Stack<>();
    OnBackPressedCallback backPressed = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (fragmentHistory.empty()) return;
            replaceFragment(fragmentHistory.pop());
        }
    };

    ActivityUniversityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUniversityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getOnBackPressedDispatcher().addCallback(backPressed);

        status = JWTValidation.getTokenProperty(this, "verified");
        initialise();
    }

    public void initialise() {
        if (Objects.equals(status, "Verified")) {
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    replaceFragment(new UniversityHomeFragment());
                } else if (itemId == R.id.search) {
                    replaceFragment(new SearchUniversityFragment());
                } else if (itemId == R.id.applications) {
                    replaceFragment(new ApplicationsFragment());
                }
                return true;
            });

            replaceFragment(new UniversityHomeFragment());
        } else {
            binding.bottomNavigationView.setVisibility(View.GONE);
            replaceFragment(Objects.equals(status, "Submitted") ?
                    new HomeSubmittedFragment() : new HomeUnverifiedFragment());
        }
    }
}
