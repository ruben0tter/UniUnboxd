package com.example.uniunboxd.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.R;
import com.example.uniunboxd.fragments.student.SearchFragment;
import com.example.uniunboxd.utilities.UserState;
import com.example.uniunboxd.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserState state = new UserState("userToken");
        replaceFragment(state.getHomeFragment());
        setUserState(state);

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
    
    
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

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
}
