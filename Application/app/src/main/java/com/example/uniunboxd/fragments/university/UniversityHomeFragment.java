package com.example.uniunboxd.fragments.university;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.models.home.OverviewCourse;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UniversityHomeFragment extends Fragment implements View.OnClickListener {
    // Define layout variables
    LinearLayout coursesLayout;

    // Define data variables
    List<OverviewCourse> overviewCourses;

    /**
     * Necessary empty constructor.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uni_home, container, false);

        // The course layout
        coursesLayout = view.findViewById(R.id.linearLayoutforCourses);

        // Sign out button
        TextView signOut = view.findViewById(R.id.signOut);
        signOut.setOnClickListener(this);
        // Add courses button
        TextView addCourses = view.findViewById(R.id.addCourses);
        addCourses.setOnClickListener(this);

        // University name
        TextView universityName = view.findViewById(R.id.university_name_text);
        universityName.setText(JWTValidation.getTokenProperty(getActivity(), "name"));

        CoursesInformation coursesInformation = new CoursesInformation();

        try {
            // Get the courses information
            overviewCourses = coursesInformation.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Add the courses to the layout
        if (overviewCourses != null) {
            for (OverviewCourse c : overviewCourses) {
                coursesLayout.addView(c.createView(inflater, container, this));
            }
        }
        return view;
    }

    /**
     * Handles the sign out and add courses button click event.
     *
     * @param v The view.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signOut) {
            // Sign out
            JWTValidation.deleteToken(getActivity());
            ((IActivity) getActivity()).replaceActivity(MainActivity.class);
        } else if (v.getId() == R.id.addCourses) {
            ((IActivity) getActivity()).replaceFragment(new CreateCourseFragment(), true);
        }
    }

    /**
     * AsyncTask to get the courses information.
     */
    class CoursesInformation extends AsyncTask<FragmentActivity, Void, List<OverviewCourse>> {
        @Override
        protected List<OverviewCourse> doInBackground(FragmentActivity... fragmentActivities) {
            try {
                // Get the last edited courses by university from the API.
                return CourseController.getLastEditedCoursesByUniversity(fragmentActivities[0]);
            } catch (Exception e) {
                Log.e("ERR", "Couldn't get review" + e);
                return null;
            }
        }
    }
}
