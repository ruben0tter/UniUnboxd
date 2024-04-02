package com.example.uniunboxd.activities;

import android.os.Bundle;
import android.view.View;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityUniversityBinding;
import com.example.uniunboxd.fragments.university.ApplicationsFragment;
import com.example.uniunboxd.fragments.university.HomeSubmittedFragment;
import com.example.uniunboxd.fragments.university.HomeUnverifiedFragment;
import com.example.uniunboxd.fragments.university.SearchUniversityFragment;
import com.example.uniunboxd.fragments.university.UniversityHomeFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.StackHandler;

import java.util.Objects;

public class UniversityActivity extends IActivity {

    String status;

    ActivityUniversityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUniversityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getOnBackPressedDispatcher().addCallback(backPressed);

        StackHandler stackHandler = StackHandler.getInstance();
        if(stackHandler.stack != null && !stackHandler.stack.empty()) {
            fragmentHistory = stackHandler.stack;
            goBack();
        } else {
            status = JWTValidation.getTokenProperty(this, "verified");
            initialise();
        }
    }

    public void initialise() {
        if (Objects.equals(status, "Verified")) {
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    replaceFragment(new UniversityHomeFragment(), false);
                } else if (itemId == R.id.search) {
                    replaceFragment(new SearchUniversityFragment(), false);
                } else if (itemId == R.id.applications) {
                    replaceFragment(new ApplicationsFragment(), false);
                }
                fragmentHistory.removeAllElements();
                return true;
            });

            replaceFragment(new UniversityHomeFragment(), false);
        } else {
            binding.bottomNavigationView.setVisibility(View.GONE);
            replaceFragment(Objects.equals(status, "Submitted") ?
                    new HomeSubmittedFragment() : new HomeUnverifiedFragment(), false);
        }
    }
}
