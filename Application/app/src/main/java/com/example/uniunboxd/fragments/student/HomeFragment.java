package com.example.uniunboxd.fragments.student;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.models.home.PopularCourse;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment implements View.OnClickListener {
    LinearLayout popularCoursesLayout;
    LinearLayout newFromFriendsLayout;
    LinearLayout popularCoursesWithFriendsLayout;

    List<PopularCourse> popularCourses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        // Layouts
        popularCoursesLayout = view.findViewById(R.id.popularCourses);
        newFromFriendsLayout = view.findViewById(R.id.newFromFriends);
        popularCoursesWithFriendsLayout = view.findViewById(R.id.popularCoursesWithFriends);

        PopularCoursesInformation popularCoursesInformation;

        String attachedUniversityId = JWTValidation.getTokenProperty(getActivity(), "university");
        if(attachedUniversityId == null) {
            popularCoursesInformation = new PopularCoursesInformation();
        } else {
            popularCoursesInformation = new PopularCoursesInformation(Integer.parseInt(attachedUniversityId));
        }

        try {
            popularCourses = popularCoursesInformation.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (popularCourses != null) {
            for(PopularCourse c : popularCourses) {
                popularCoursesLayout.addView(c.createView(inflater, container, this));
            }
        }

        return view;

    }
    @Override
    public void onClick(View view) {
    }
}

class PopularCoursesInformation extends AsyncTask<FragmentActivity, Void, List<PopularCourse>> {
    private final int id;

    public PopularCoursesInformation() {
        this.id = 0;
    }

    public PopularCoursesInformation(int id) {
        this.id = id;
    }

    @Override
    protected List<PopularCourse> doInBackground(FragmentActivity... fragmentActivities) {
        try{
            if(id == 0) {
                return CourseController.getPopularCourses(fragmentActivities[0]);
            } else {
                return CourseController.getPopularCoursesByUniversity(id, fragmentActivities[0]);
            }

        } catch(Exception e) {
            Log.e("ERR", "Couldn't get review" + e.toString());
            return null;
        }
    }
}
