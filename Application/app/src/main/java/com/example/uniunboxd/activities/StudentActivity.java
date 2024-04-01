package com.example.uniunboxd.activities;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityStudentBinding;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.fragments.student.SearchStudentFragment;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.util.Objects;

public class StudentActivity extends IActivity {

    ActivityStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNavigationMenu();

        getOnBackPressedDispatcher().addCallback(backPressed);

        replaceFragment(new HomeFragment(), false);

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
                replaceFragment(new StudentProfileFragment(userId), true);
            }
            fragmentHistory.removeAllElements();
            return true;
        });
    }

    private void askNotificationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1234);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    return;
                                }
                                if (task.getResult() != null) {
                                    String deviceToken = Objects.requireNonNull(task.getResult());
                                    FragmentActivity f = this;

                                    AsyncTask.execute(() -> {
                                        try {
                                            HttpURLConnection con = UserController.setDeviceToken(deviceToken, f);

                                            Log.i("DeviceToken", "Code: " + con.getResponseCode());
                                            Log.i("DeviceToken", "Message: " + con.getResponseMessage());
                                        } catch (Exception e) {
                                            Log.e("DeviceToken", e.toString());
                                        }

                                    });
                                }
                            });
                }
            });
}