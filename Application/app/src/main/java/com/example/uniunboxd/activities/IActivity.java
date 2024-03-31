package com.example.uniunboxd.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.Redirection;

import java.util.Stack;

public abstract class IActivity extends AppCompatActivity {

    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Redirection.replaceActivity(this, activity);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public Stack<Fragment> fragmentHistory = new Stack<>();

    public void replaceFragment(Fragment fragment, boolean remember) {
        if (remember) fragmentHistory.add(fragment);

        replaceFragment(fragment);
    }
}
