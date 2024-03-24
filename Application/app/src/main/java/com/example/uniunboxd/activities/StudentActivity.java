package com.example.uniunboxd.activities;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityStudentBinding;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.fragments.student.SearchStudentFragment;
import com.example.uniunboxd.utilities.Redirection;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.util.Objects;

public class StudentActivity extends AppCompatActivity implements IActivity {

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

    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Redirection.replaceActivity(this, activity);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void askNotificationPermission() {
        // TODO: Figure out how to do this for API Level < 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                FirebaseMessaging.getInstance().getToken ()
                    .addOnCompleteListener ( task -> {
                        if (!task.isSuccessful()) { return; }
                        if (task.getResult() != null) {
                            String deviceToken = Objects.requireNonNull ( task.getResult() );
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
                    } );
            }
        });
}