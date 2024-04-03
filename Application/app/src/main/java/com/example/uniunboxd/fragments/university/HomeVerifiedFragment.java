package com.example.uniunboxd.fragments.university;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.utilities.JWTValidation;

public class HomeVerifiedFragment extends Fragment {
    public HomeVerifiedFragment() {
        // Empty constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_uni_verified, container, false);
        Button createBtn = (Button) view.findViewById(R.id.createCourse);
        Button signOut = (Button) view.findViewById(R.id.signOut);

        createBtn.setOnClickListener(v -> {
            Log.w("dasnjkads", "dnajskdas");
            AsyncTask.execute(() -> {
                ((IActivity) getActivity()).replaceFragment(new CreateCourseFragment(), true);
            });
        });

        signOut.setOnClickListener(v -> {
            JWTValidation.deleteToken(getActivity());
            ((IActivity) getActivity()).replaceActivity(MainActivity.class);
        });

        return view;
    }
}
