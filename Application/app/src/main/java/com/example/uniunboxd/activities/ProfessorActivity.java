package com.example.uniunboxd.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityStudentBinding;
import com.example.uniunboxd.fragments.professor.ProfessorProfileFragment;
import com.example.uniunboxd.fragments.student.SearchFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.Redirection;

public class ProfessorActivity extends AppCompatActivity implements IActivity{
    ActivityStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNavigationMenu();

        int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
        replaceFragment(new ProfessorProfileFragment(id));
    }

    @Override
    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Redirection.replaceActivity(this, activity);
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    public void setNavigationMenu() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.profile) {
                int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
                replaceFragment(new ProfessorProfileFragment(id));
            } else if (itemId == R.id.applications) {
                //TODO: Link professor search
                replaceFragment(new SearchFragment());
            }
            return true;
        });
    }
}
