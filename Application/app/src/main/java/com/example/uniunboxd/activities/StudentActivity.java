package com.example.uniunboxd.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityStudentBinding;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.SearchStudentFragment;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.StackHandler;
import com.google.firebase.messaging.FirebaseMessaging;

public class StudentActivity extends IActivity {

    ActivityStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNavigationMenu();

        getOnBackPressedDispatcher().addCallback(backPressed);

        StackHandler stackHandler = StackHandler.getInstance();
        if (stackHandler.stack != null && !stackHandler.empty()) {
            fragmentHistory = stackHandler.stack;
            goBack();
        } else {
            replaceFragment(new HomeFragment(), false);
        }

        askNotificationPermission();
    }

    public void setNavigationMenu() {
        int userId = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment(), false);
            } else if (itemId == R.id.search) {
                replaceFragment(new SearchStudentFragment(), false);
            } else if (itemId == R.id.profile) {
                replaceFragment(new StudentProfileFragment(userId), false);
            }
            fragmentHistory.removeAllElements();
            return true;
        });
    }

    static final int NOTIFICATION_REQUEST_CODE = 1234;

    private void askNotificationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != NOTIFICATION_REQUEST_CODE) return;
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) return;

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) return;
                    AsyncTask.execute(() -> {
                        try {
                            UserController.setDeviceToken(task.getResult(), this);
                        } catch (Exception e) {
                            Log.e("DeviceToken", e.toString());
                        }
                    });
                });
    }
}
