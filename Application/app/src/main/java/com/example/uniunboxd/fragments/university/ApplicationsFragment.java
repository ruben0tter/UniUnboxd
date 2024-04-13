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

/**
 * ApplicationsFragment class that represents the applications screen.
 */
public class ApplicationsFragment extends Fragment {
    private final List<Application> applications = new ArrayList<>();

    /**
     * Creates the view for the applications fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the applications fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View listView = inflater.inflate(R.layout.fragment_profile_uni, container, false);
        LinearLayout layout = listView.findViewById(R.id.applications_list);
        Button loadButton = listView.findViewById(R.id.load_more_button);
        loadApplications(inflater, layout);
        // Load more applications when the button is clicked.
        loadButton.setOnClickListener(v -> loadApplications(inflater, layout));
        return listView;
    }

    /**
     * Loads the applications.
     *
     * @param inflater The layout inflater.
     * @param layout   The parent layout.
     */
    private void loadApplications(LayoutInflater inflater, LinearLayout layout) {
        // Load applications in a separate thread.
        AsyncTask.execute(() -> {
            int lastID = 0;
            // Get the last application ID.
            if (!applications.isEmpty()) {
                lastID = applications.get(applications.size() - 1).ID;
            }
            try {
                // Get the new applications that are pending with a request to the API.
                List<Application> newApplications = VerificationController.getPendingApplications(lastID, getActivity());
                applications.addAll(newApplications);
                // Add the new applications to the layout.
                for (Application app : newApplications) {
                    getActivity().runOnUiThread(() -> {
                        // Create a view for the application.
                        View appView = app.createView(inflater, layout, getActivity());
                        layout.addView(appView);
                    });
                }
            } catch (Exception e) {
                // Log any errors that occur.
                Log.e("LoadApplications", e.toString());
            }
        });
    }
}
