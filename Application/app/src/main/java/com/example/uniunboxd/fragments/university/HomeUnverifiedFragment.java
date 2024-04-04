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

// TODO: Unwire this shit from HomeFragmemt
public class HomeUnverifiedFragment extends Fragment {
    public HomeUnverifiedFragment() {
        // Empty constructor.
    }

    static final int MAX_FILE_COUNT = 3;

    static final int PICKFILE_RESULT_CODE = 42069;
    Button btnApply;
    Button[] btnUploads = new Button[MAX_FILE_COUNT];
    Button signOut;

    private final byte[][] verificationFiles = new byte[MAX_FILE_COUNT][];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_uni_unverified, container, false);
        btnUploads[0] = view.findViewById(R.id.uploadFile1);
        btnUploads[1] = view.findViewById(R.id.uploadFile2);
        btnUploads[2] = view.findViewById(R.id.uploadFile3);

        btnApply = view.findViewById(R.id.submitApplication);
        signOut = view.findViewById(R.id.signOut);
        btnApply.setEnabled(false);

        btnApply.setOnClickListener(v -> {
            btnApply.setEnabled(false);
            AsyncTask.execute(() -> {
                try {
                    VerificationController.sendApplication(verificationFiles, getActivity());
                    reload();
                } catch (Exception e) {
                    Log.e("APP", "Failed to upload documents: " + e);
                }
            });
        });

        Fragment f = this;

        for (int i = 0; i < btnUploads.length; i++) {
            int finalI = i;
            btnUploads[i].setOnClickListener(v -> FileSystemChooser.ChoosePDF(f, PICKFILE_RESULT_CODE + finalI));
        }


        signOut.setOnClickListener(v -> {
            JWTValidation.deleteToken(getActivity());
            ((IActivity) getActivity()).replaceActivity(MainActivity.class);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int idx = requestCode - PICKFILE_RESULT_CODE;
        if (resultCode == Activity.RESULT_OK && (idx >= 0 && idx <= 2)) {
            Uri uri = data.getData();
            try {
                verificationFiles[idx] = FileSystemChooser.readTextFromUri(uri, getActivity());
                btnApply.setEnabled(true);

                Button btn = btnUploads[idx];
                btn.setText("Change file");
            } catch (Exception err) {
                Log.e("APP", "Failed to read file: " + err);
            }
        }
    }

    private void reload() {
        try {
            ((IActivity) getActivity()).replaceFragment(new HomeSubmittedFragment(), false);
        } catch (Exception e) {
            Log.d("ERR", "i dunno");
        }
    }
}
