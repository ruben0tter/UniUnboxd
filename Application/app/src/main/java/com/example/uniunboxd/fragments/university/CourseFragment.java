package com.example.uniunboxd.fragments.university;

import static android.view.View.GONE;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.ReviewListItem;
import com.example.uniunboxd.models.course.CourseEditModel;
import com.example.uniunboxd.models.course.CourseRetrievalModel;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * CourseFragment class that represents the course screen.
 */
public class CourseFragment extends Fragment {

    private int ID;
    private final int NUM_REVIEWS_TO_LOAD = 5;
    public CourseRetrievalModel Course = null;

    /**
     * Necessary empty constructor.
     */
    public CourseFragment() {
    }

    /**
     * Constructor for the CourseFragment class.
     *
     * @param id The course's ID.
     */
    public CourseFragment(int id) {
        this.ID = id;
    }

    /**
     * Creates the view for the course fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the course fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = null;
        GetCourseInformationAsyncTask asyncGetTask = new GetCourseInformationAsyncTask(ID, NUM_REVIEWS_TO_LOAD);
        try {
            Course = asyncGetTask.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If the course is not null, create the view.
        if (Course != null) {
            view = Course.createView(inflater, container, getActivity());

            Button loadBtn = view.findViewById(R.id.load);
            LinearLayout reviewList = view.findViewById(R.id.reviewList);

            ImageButton editBtn = view.findViewById(R.id.editButton);
            loadBtn.setOnClickListener(v -> load(reviewList, inflater, container));

            editBtn.setOnClickListener(v -> {
                CourseEditModel editModel = new CourseEditModel(Course.Id, Course.Name, Course.Code, Course.Description, Course.Professor, Course.Image, Course.Banner, new ArrayList<>());
                ((IActivity) getActivity()).replaceFragment(new CreateCourseFragment(editModel), true);
            });

            // Get role and user id from token.
            String role = JWTValidation.getTokenProperty(getActivity(), "typ");
            int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));

            // If the user is a student or a professor who is not assigned to the course, hide the edit button.
            if (role.equals("Student") || (role.equals("Professor") && !Course.AssignedProfessors.contains(userId)))
                editBtn.setVisibility(GONE);
        }
        return view;
    }

    /**
     * Loads the reviews.
     * @param reviewListView The review list view.
     * @param inflater The layout inflater.
     * @param container The parent layout.
    */
    private void load(LinearLayout reviewListView, LayoutInflater inflater, ViewGroup container) {
        AsyncTask.execute(() -> {
            int lastID = 0;
            if (!Course.Reviews.isEmpty()) {
                lastID = Course.Reviews.get(Course.Reviews.size() - 1).Id;
            }
            try {
                List<ReviewListItem> newReviews = ReviewController.getReviewListItems(lastID, Course.Id, 5, getActivity());
                Course.Reviews.addAll(newReviews);
                visualiseReviews(inflater, container, reviewListView);
            } catch (Exception e) {
                Log.e("APP", e.toString());
            }
        });
    }

    /**
     * Visualises the reviews.
     * @param inflater The layout inflater.
     * @param container The parent layout.
     * @param reviewListView The review list view.
     */
    private void visualiseReviews(LayoutInflater inflater, ViewGroup container, LinearLayout reviewListView) {
        for (ReviewListItem item : Course.Reviews) {
            getActivity().runOnUiThread(() -> {
                View appView = item.createView(inflater, container);
                reviewListView.addView(appView);
            });
        }
    }

    /**
     * AsyncTask to get course information.
     */
    class GetCourseInformationAsyncTask extends AsyncTask<FragmentActivity, Void, CourseRetrievalModel> {

        private final int ID;
        private final int NUM_OF_REVIEWS_TO_LOAD;

        /**
         * Constructor for the GetCourseInformationAsyncTask class.
         *
         * @param id The course's ID.
         * @param numReviewsToLoad The number of reviews to load.
         */
        public GetCourseInformationAsyncTask(int id, int numReviewsToLoad) {
            ID = id;
            NUM_OF_REVIEWS_TO_LOAD = numReviewsToLoad;
        }

        /**
         * Gets the course information.
         *
         * @param fragments The fragments.
         * @return The course information.
         */
        @Override
        protected CourseRetrievalModel doInBackground(FragmentActivity... fragments) {
            CourseRetrievalModel course = null;
            try {
                course = CourseController.getCourseById(ID, NUM_OF_REVIEWS_TO_LOAD, fragments[0]);
            } catch (Exception ioe) {
                Log.e("ERR", "Couldn't get course" + ioe);
            }

            Course = course;
            return course;
        }
    }
}



