package com.example.uniunboxd.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public interface IActivity {
    void replaceActivity(Class<? extends AppCompatActivity> activity);
    void replaceFragment(Fragment fragment);
}
