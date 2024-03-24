package com.example.uniunboxd.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityUniversityBinding;
import com.example.uniunboxd.fragments.university.ApplicationsFragment;
import com.example.uniunboxd.fragments.university.HomeSubmittedFragment;
import com.example.uniunboxd.fragments.university.HomeUnverifiedFragment;
import com.example.uniunboxd.fragments.university.SearchUniversityFragment;
import com.example.uniunboxd.fragments.university.UniversityHomeFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.Redirection;

import java.util.Objects;

public class UniversityActivity extends AppCompatActivity implements IActivity {

    ActivityUniversityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUniversityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNavigationMenu();

        replaceFragment(getCorrectHomeFragment());
    }

    private Fragment getCorrectHomeFragment() {
        String state = JWTValidation.getTokenProperty(this, "verified");

        if (Objects.equals(state, "Verified")) {
            setNavigationMenu();
            return new UniversityHomeFragment();
        } else if (Objects.equals(state, "Pending")) {
            binding.bottomNavigationView.setVisibility(View.GONE);
            return new HomeSubmittedFragment();
        } else {
            binding.bottomNavigationView.setVisibility(View.GONE);
            return new HomeUnverifiedFragment();
        }
    }
    
    public void setNavigationMenu() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(getCorrectHomeFragment());
            } else if (itemId == R.id.search) {
                replaceFragment(new SearchUniversityFragment());
            } else if (itemId == R.id.applications) {
                replaceFragment(new ApplicationsFragment());
            }
            return true;
        });
    }

    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Redirection.replaceActivity(this, activity);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}