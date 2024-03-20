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
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.JWTValidation;

// TODO: Unwire this shit from HomeFragmemt
public class HomeUnverifiedFragment extends HomeFragment {
    public HomeUnverifiedFragment() {
        // Empty constructor.
    }

    static final int PICKFILE_RESULT_CODE = 42069;
    Button btnApply;
    Button btnUpload1;
    Button btnUpload2;
    Button btnUpload3;
    Button signOut;

    private byte[][] verificationFiles = new byte[3][];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_uni_unverified, container, false);
        btnUpload1 = (Button) view.findViewById(R.id.uploadFile1);
        btnUpload2 = (Button) view.findViewById(R.id.uploadFile2);
        btnUpload3 = (Button) view.findViewById(R.id.uploadFile3);

        btnApply = (Button) view.findViewById(R.id.submitApplication);
        signOut = (Button) view.findViewById(R.id.signOut);
        btnApply.setEnabled(false);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            VerificationController.sendApplication(verificationFiles, getActivity());
                            reload();
                        } catch (Exception e) {
                            Log.e("APP", "Failed to upload documents: " + e.toString());
                        }

                    }
                });
            }
        });

        Fragment f = this;
        btnUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSystemChooser.ChoosePDF(f, PICKFILE_RESULT_CODE);
            }
        });
        btnUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSystemChooser.ChoosePDF(f, PICKFILE_RESULT_CODE + 1);
            }
        });

        btnUpload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSystemChooser.ChoosePDF(f, PICKFILE_RESULT_CODE + 2);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JWTValidation.deleteToken(getActivity());
                ((IActivity) getActivity()).replaceActivity(MainActivity.class);
            }
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
            } catch (Exception err) {
                Log.e("APP", "Failed to read file: " + err);
            }
        }
    }

    private void reload() {
        try {
            replaceFragment(new HomeSubmittedFragment());
            //((StudentActivity) getActivity()).setUserState(new UserState("submitted"));
        } catch (Exception e) {
            Log.d("ERR", "i dunno");
        }
    }
}
