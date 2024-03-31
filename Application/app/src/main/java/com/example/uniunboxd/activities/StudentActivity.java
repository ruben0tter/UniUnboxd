package com.example.uniunboxd.activities;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityStudentBinding;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.fragments.student.SearchStudentFragment;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.Stack;

public class StudentActivity extends IActivity {

    Stack<Fragment> fragmentHistory = new Stack<>();
    OnBackPressedCallback backPressed = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (fragmentHistory.empty()) return;
            replaceFragment(fragmentHistory.pop());
        }
    };
    ActivityStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNavigationMenu();

        replaceFragment(new HomeFragment());

        askNotificationPermission();
    }

    public void setNavigationMenu() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.search) {
                replaceFragment(new SearchStudentFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
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