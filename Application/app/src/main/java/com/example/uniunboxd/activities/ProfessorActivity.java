package com.example.uniunboxd.activities;

import android.os.Bundle;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityProfessorBinding;
import com.example.uniunboxd.fragments.professor.ProfessorProfileFragment;
import com.example.uniunboxd.fragments.professor.SearchProfessorFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.StackHandler;

public class ProfessorActivity extends IActivity {
    ActivityProfessorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfessorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getOnBackPressedDispatcher().addCallback(backPressed);

        setNavigationMenu();

        StackHandler stackHandler = StackHandler.getInstance();
        if (stackHandler.empty()) {
            fragmentHistory = stackHandler;
            goBack();
        } else {
            int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
            replaceFragment(new ProfessorProfileFragment(id), false);
        }
    }

    public void setNavigationMenu() {
        binding.bottomNavigationViewProfessor.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.profile) {
                int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
                replaceFragment(new ProfessorProfileFragment(id), false);
            } else if (itemId == R.id.search) {
                //TODO: Link professor search
                replaceFragment(new SearchProfessorFragment(), false);
            }
            return true;
        });
    }
}
