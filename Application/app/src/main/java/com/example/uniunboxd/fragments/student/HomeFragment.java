package com.example.uniunboxd.fragments.student;

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
import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.models.home.FriendReview;
import com.example.uniunboxd.models.home.PopularCourse;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * HomeFragment class that represents the home screen.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // Define layout variables
    LinearLayout popularCoursesLayout; // Layout for popular courses
    LinearLayout newFromFriendsLayout; // Layout for new reviews from friends
    LinearLayout popularCoursesWithFriendsLayout; // Layout for popular courses with friends

    // Define data variables
    List<PopularCourse> popularCourses; // List of popular courses
    List<FriendReview> newFromFriends; // List of new reviews from friends
    List<PopularCourse> popularCoursesWithFriends; // List of popular courses with friends


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the home fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the home fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        // Layouts
        popularCoursesLayout = view.findViewById(R.id.popularCourses);
        newFromFriendsLayout = view.findViewById(R.id.newFromFriends);
        popularCoursesWithFriendsLayout = view.findViewById(R.id.popularCoursesWithFriends);

        // Button
        TextView signOut = view.findViewById(R.id.signOut);
        signOut.setOnClickListener(this);

        PopularCoursesInformation popularCoursesInformation;
        LatestReviewsByFriendsInformation newFromFriendsInformation = new LatestReviewsByFriendsInformation();
        PopularCoursesByFriendsInformation popularCoursesByFriendsInformation = new PopularCoursesByFriendsInformation();

        // Get your university's id from token.
        String attachedUniversityId = JWTValidation.getTokenProperty(getActivity(), "university");
        if (attachedUniversityId == null) {
            // If the user is not attached to a university, get the popular courses from all universities.
            popularCoursesInformation = new PopularCoursesInformation();
        } else {
            // If the user is attached to a university, get the popular courses from that university.
            popularCoursesInformation = new PopularCoursesInformation(Integer.parseInt(attachedUniversityId));
        }

        try {
            // Execute the async tasks to get popular courses, new reviews from friends, and popular courses with friends.
            popularCourses = popularCoursesInformation.execute(getActivity()).get();
            newFromFriends = newFromFriendsInformation.execute(getActivity()).get();
            popularCoursesWithFriends = popularCoursesByFriendsInformation.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Add popular courses, new reviews from friends, and popular courses with friends to the layout.
        if (popularCourses != null) {
            for (PopularCourse c : popularCourses) {
                popularCoursesLayout.addView(c.createView(inflater, container, this));
            }
        }
        // Add new reviews from friends to the layout.
        if (newFromFriends != null) {
            for (FriendReview r : newFromFriends) {
                newFromFriendsLayout.addView(r.createView(inflater, container, this));
            }
        }
        // Add popular courses with friends to the layout.
        if (popularCoursesWithFriends != null) {
            for (PopularCourse c : popularCoursesWithFriends) {
                popularCoursesWithFriendsLayout.addView(c.createView(inflater, container, this));
            }
        }

        return view;

    }

    /**
     * Handles the click event for the sign out button.
     *
     * @param view The view.
     */
    @Override
    public void onClick(View view) {
        // Sign out.
        JWTValidation.deleteToken(getActivity());
        ((IActivity) getActivity()).replaceActivity(MainActivity.class);
    }
}

// Async tasks to get popular courses, latest reviews from friends, and popular courses with friends.
class PopularCoursesInformation extends AsyncTask<FragmentActivity, Void, List<PopularCourse>> {
    private final int id;

    // Constructor
    public PopularCoursesInformation() {
        this.id = 0;
    }

    public PopularCoursesInformation(int id) {
        this.id = id;
    }

    // Get popular courses.
    @Override
    protected List<PopularCourse> doInBackground(FragmentActivity... fragmentActivities) {
        // Get popular courses.
        try {
            if (id == 0) {
                // Get popular courses from all universities.
                return CourseController.getPopularCourses(fragmentActivities[0]);
            } else {
                // Get popular courses from a specific university.
                return CourseController.getPopularCoursesByUniversity(id, fragmentActivities[0]);
            }
        } catch (Exception e) {
            Log.e("ERR", "Couldn't get review" + e);
            return null;
        }
    }
}

// Async tasks to get the latest reviews from friends and popular courses with friends.
class LatestReviewsByFriendsInformation extends AsyncTask<FragmentActivity, Void, List<FriendReview>> {
    @Override
    protected List<FriendReview> doInBackground(FragmentActivity... fragmentActivities) {
        try {
            // Get the latest reviews from friends.
            return ReviewController.getLatestReviewsByFriends(fragmentActivities[0]);
        } catch (Exception e) {
            Log.e("ERR", "Couldn't get review" + e);
            return null;
        }
    }
}

// Async tasks to get popular courses with friends.
class PopularCoursesByFriendsInformation extends AsyncTask<FragmentActivity, Void, List<PopularCourse>> {
    @Override
    protected List<PopularCourse> doInBackground(FragmentActivity... fragmentActivities) {
        try {
            // Get popular courses with friends.
            return CourseController.getPopularCoursesByFriends(fragmentActivities[0]);
        } catch (Exception e) {
            Log.e("ERR", "Couldn't get review" + e);
            return null;
        }
    }
}
