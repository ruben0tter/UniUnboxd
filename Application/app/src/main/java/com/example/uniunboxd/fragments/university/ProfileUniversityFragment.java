package com.example.uniunboxd.fragments.university;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.models.Application;

import java.util.ArrayList;
import java.util.List;

public class ProfileUniversityFragment extends ProfileFragment {
    private final List<Application> applications = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View listView = inflater.inflate(R.layout.fragment_profile_uni, container, false);
        LinearLayout layout = listView.findViewById(R.id.applications_list);
        Button loadButton = listView.findViewById(R.id.load_more_button);
        loadApplications(inflater, layout);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApplications(inflater, layout);
            }
        });
        return listView;
    }

    private void loadApplications(LayoutInflater inflater, LinearLayout layout) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int lastID = 0;
                if (!applications.isEmpty()) {
                    lastID = applications.get(applications.size() - 1).ID;
                }
                try {
                    List<Application> newApplications = VerificationController.getPendingApplications(lastID);
                    applications.addAll(newApplications);
                    for (Application app : newApplications) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override

                            public void run() {
                                View appView = app.createView(inflater, layout, getActivity());
                                layout.addView(appView);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("APP", e.toString());
                }
            }
        });
    }
}
