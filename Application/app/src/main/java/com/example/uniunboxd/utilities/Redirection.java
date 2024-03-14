package com.example.uniunboxd.utilities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;

public class Redirection {
    public static void replaceActivity(AppCompatActivity current,
                                       Class<? extends AppCompatActivity> next ) {
        Intent i = new Intent(current.getApplicationContext(), next);
        current.startActivity(i);
    }

    public static void replaceFragment(AppCompatActivity c, Fragment fragment) {
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
