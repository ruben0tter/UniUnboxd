package com.example.uniunboxd.activities;

import androidx.appcompat.app.AppCompatActivity;

public interface IActivity {
    void replaceActivity(Class<? extends AppCompatActivity> activity);
}
