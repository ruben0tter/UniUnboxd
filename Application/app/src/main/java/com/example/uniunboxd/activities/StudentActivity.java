package com.example.uniunboxd.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityStudentBinding;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.fragments.student.SearchFragment;
import com.example.uniunboxd.utilities.Redirection;

public class StudentActivity extends AppCompatActivity implements IActivity {

    ActivityStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNavigationMenu();

        replaceFragment(new HomeFragment());

        /*
        UserState state = new UserState("userToken");
        replaceFragment(state.getHomeFragment());
        setUserState(state);
        */
    }

    public void setNavigationMenu() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
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

    /*
        public void setUserState(UserState strategy) {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(strategy.getHomeFragment());
            } else if (itemId == R.id.search) {
                replaceFragment(strategy.getSearchFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(strategy.getProfileFragment());
            }
            return true;
        });
    }
     */
}