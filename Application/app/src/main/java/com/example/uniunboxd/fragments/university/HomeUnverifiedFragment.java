package com.example.uniunboxd.fragments.university;

import static androidx.core.app.ActivityCompat.startActivityForResult;

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
import androidx.fragment.app.FragmentManager;

import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.JWTValidation;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeUnverifiedFragment extends HomeFragment {
    public HomeUnverifiedFragment() {
        // Empty constructor.
    }

    static final int PICKFILE_RESULT_CODE = 42069;
    Button btnApply;
    Button btnUpload1;
    Button btnUpload2;

    private List<byte[]> verificationFileContents = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_uni_unverified, container, false);
        btnUpload1 = (Button) view.findViewById(R.id.selectApplicationData1);
        btnUpload2 = (Button) view.findViewById(R.id.selectApplicationData2);
        btnApply = (Button) view.findViewById(R.id.submitApplication);
        Button signOut = (Button) view.findViewById(R.id.signOut);
        btnApply.setEnabled(false);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            VerificationController.sendApplication(verificationFileContents, getActivity());
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
                FileSystemChooser.ChoosePDF(f);
            }
        });
        btnUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSystemChooser.ChoosePDF(f);
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
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                verificationFileContents.add(FileSystemChooser.readTextFromUri(uri, getActivity()));
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
