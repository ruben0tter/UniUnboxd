package com.example.uniunboxd.fragments.university;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.models.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsFragment extends Fragment {
    private final List<Application> applications = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View listView = inflater.inflate(R.layout.fragment_profile_uni, container, false);
        LinearLayout layout = listView.findViewById(R.id.applications_list);
        Button loadButton = listView.findViewById(R.id.load_more_button);
        loadApplications(inflater, layout);
        loadButton.setOnClickListener(v -> loadApplications(inflater, layout));
        return listView;
    }

    private void loadApplications(LayoutInflater inflater, LinearLayout layout) {
        AsyncTask.execute(() -> {
            int lastID = 0;
            if (!applications.isEmpty()) {
                lastID = applications.get(applications.size() - 1).ID;
            }
            try {
                List<Application> newApplications = VerificationController.getPendingApplications(lastID, getActivity());
                applications.addAll(newApplications);
                for (Application app : newApplications) {
                    getActivity().runOnUiThread(() -> {
                        View appView = app.createView(inflater, layout, getActivity());
                        layout.addView(appView);
                    });
                }
            } catch (Exception e) {
                Log.e("LoadApplications", e.toString());
            }
        });
    }
}
