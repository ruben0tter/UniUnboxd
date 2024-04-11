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

/**
 * StudentActivity is an activity that extends IActivity, which provides methods for handling back press, replacing activities and fragments, and saving instance state.
 * It uses ActivityStudentBinding for view binding, which replaces findViewById calls.
 * This activity is specific to the student user type and includes a navigation menu for home, search, and profile.
 * It also requests notification permissions and sets the device token for Firebase Messaging.
 */
public class StudentActivity extends IActivity {
    // Binding instance for the activity's view
    ActivityStudentBinding binding;

    // Request code for notification permissions
    static final int NOTIFICATION_REQUEST_CODE = 1234;

    /**
     * This method is called when the activity is starting.
     * It inflates the activity's layout, sets the content view, adds a back press callback, sets up the navigation menu, sets up the initial fragment, and asks for notification permissions.
     * If there are fragments in the fragment history, it goes back to the previous fragment.
     * Otherwise, it replaces the fragment with the HomeFragment.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this activity
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        // Set the inflated layout as the content view
        setContentView(binding.getRoot());

        // Set up the navigation menu
        setNavigationMenu();

        // Add a back press callback
        getOnBackPressedDispatcher().addCallback(backPressed);

        // Get the StackHandler instance
        StackHandler stackHandler = StackHandler.getInstance();
        // If there are fragments in the fragment history, go back to the previous fragment
        if (stackHandler.stack != null && !stackHandler.empty()) {
            fragmentHistory = stackHandler.stack;
            goBack();
        } else {
            // Otherwise, replace the fragment with the HomeFragment
            replaceFragment(new HomeFragment(), false);
        }

        // Ask for notification permissions
        askNotificationPermission();
    }

    /**
     * This method sets up the navigation menu.
     * It sets an item selected listener on the bottom navigation view.
     * When an item is selected, it replaces the fragment with the appropriate fragment based on the item's id and clears the fragment history.
     */
    public void setNavigationMenu() {
        int userId = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                // If the home item is selected, replace the fragment with the HomeFragment
                replaceFragment(new HomeFragment(), false);
            } else if (itemId == R.id.search) {
                // If the search item is selected, replace the fragment with the SearchStudentFragment
                replaceFragment(new SearchStudentFragment(), false);
            } else if (itemId == R.id.profile) {
                // If the profile item is selected, replace the fragment with the StudentProfileFragment
                replaceFragment(new StudentProfileFragment(userId), false);
            }
            // Clear the fragment history
            fragmentHistory.removeAllElements();
            return true;
        });
    }

    /**
     * This method asks for notification permissions.
     * It requests the POST_NOTIFICATIONS permission.
     */
    private void askNotificationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_REQUEST_CODE);
    }

    /**
     * This method is called when the user responds to the permission request.
     * If the request code is NOTIFICATION_REQUEST_CODE and the permission is granted, it gets the device token for Firebase Messaging and sets it using UserController.
     * @param requestCode The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions The requested permissions
     * @param grantResults The grant results for the corresponding permissions
     */
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
