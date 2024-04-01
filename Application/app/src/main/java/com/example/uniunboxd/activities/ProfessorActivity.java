package com.example.uniunboxd.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityProfessorBinding;
import com.example.uniunboxd.fragments.professor.ProfessorProfileFragment;
import com.example.uniunboxd.fragments.professor.SearchProfessorFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.Redirection;

public class ProfessorActivity extends IActivity {
    ActivityProfessorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfessorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNavigationMenu();

        int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
        replaceFragment(new ProfessorProfileFragment(id), true);
    }

    @Override
    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Redirection.replaceActivity(this, activity);
    }
    public void setNavigationMenu() {
        binding.bottomNavigationViewProfessor.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.profile) {
                int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
                replaceFragment(new ProfessorProfileFragment(id), true);
            } else if (itemId == R.id.search) {
                //TODO: Link professor search
                replaceFragment(new SearchProfessorFragment(), true);
            }
            return true;
        });
    }
}
