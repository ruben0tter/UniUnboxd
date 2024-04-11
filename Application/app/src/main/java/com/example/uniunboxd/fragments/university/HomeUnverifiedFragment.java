package com.example.uniunboxd.fragments.university;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.JWTValidation;

/**
 * HomeUnverifiedFragment class that represents the home unverified screen.
 */
public class HomeUnverifiedFragment extends Fragment {
    
    /**
     * Necessary empty constructor.
     */
    public HomeUnverifiedFragment() {
        // Empty constructor.
    }

    static final int MAX_FILE_COUNT = 3;

    static final int PICKFILE_RESULT_CODE = 42069;
    Button btnApply;
    Button[] btnUploads = new Button[MAX_FILE_COUNT];
    Button signOut;

    private final byte[][] verificationFiles = new byte[MAX_FILE_COUNT][];

    /**
     * Creates the view for the home unverified fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the home unverified fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_uni_unverified, container, false);

        // Initialize the upload buttons
        btnUploads[0] = view.findViewById(R.id.uploadFile1);
        btnUploads[1] = view.findViewById(R.id.uploadFile2);
        btnUploads[2] = view.findViewById(R.id.uploadFile3);

        // Initialize the apply button and sign out button
        btnApply = view.findViewById(R.id.submitApplication);
        signOut = view.findViewById(R.id.signOut);

        // Disable the apply button initially
        btnApply.setEnabled(false);

        // Apply button
        btnApply.setOnClickListener(v -> {
            btnApply.setEnabled(false);
            AsyncTask.execute(() -> {
                try {
                    // Send the application to the API.
                    VerificationController.sendApplication(verificationFiles, getActivity());
                    reload();
                } catch (Exception e) {
                    Log.e("APP", "Failed to upload documents: " + e);
                }
            });
        });

        Fragment f = this;

        // Set the listeners for the upload buttons
        for (int i = 0; i < btnUploads.length; i++) {
            int finalI = i;
            btnUploads[i].setOnClickListener(v -> FileSystemChooser.ChoosePDF(f, PICKFILE_RESULT_CODE + finalI));
        }

        // Sign out button
        signOut.setOnClickListener(v -> {
            JWTValidation.deleteToken(getActivity());
            ((IActivity) getActivity()).replaceActivity(MainActivity.class);
        });

        return view;
    }

    /**
     * Handles the activity result.
     *
     * @param requestCode The request code.
     * @param resultCode  The result code.
     * @param data        The data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get the file from the file chooser
        int idx = requestCode - PICKFILE_RESULT_CODE;
        if (resultCode == Activity.RESULT_OK && (idx >= 0 && idx <= 2)) {
            // Get the file from the file chooser
            Uri uri = data.getData();
            try {
                // Read the file
                verificationFiles[idx] = FileSystemChooser.readTextFromUri(uri, getActivity());
                btnApply.setEnabled(true);

                // Change the button text
                Button btn = btnUploads[idx];
                btn.setText("Change file");
            } catch (Exception err) {
                // Log the error
                Log.e("APP", "Failed to read file: " + err);
            }
        }
    }

    private void reload() {
        try {
            // Replace the fragment with the home submitted fragment.
            ((IActivity) getActivity()).replaceFragment(new HomeSubmittedFragment(), false);
        } catch (Exception e) {
            Log.d("ERR", "i dunno");
        }
    }
}
