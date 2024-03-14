package com.example.uniunboxd.fragments.student;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.models.Application;
import com.example.uniunboxd.models.CourseRetrievalModel;
import com.example.uniunboxd.models.ReviewListItem;

import java.net.HttpURLConnection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CourseFragment extends Fragment{

    private CourseRetrievalModel Course = null;

    public CourseFragment(int id) {
        // TODO: Get course information
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = null;
        AsyncGetTask asyncGetTask = new AsyncGetTask();
        try {
            Course = asyncGetTask.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(Course != null) {
            view = Course.createView(inflater, container, savedInstanceState);
        }
        if(view != null) {
            Button load = (Button) view.findViewById(R.id.load);
            LinearLayout reviewList = view.findViewById(R.id.reviewList);
            load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    load(reviewList, inflater, container, savedInstanceState);
                }
            });
        }
        return view;
    }

    private void load(LinearLayout reviewListView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int lastID = 0;
                if (!Course.Reviews.isEmpty()) {
                    lastID = Course.Reviews.get(Course.Reviews.size() - 1).Id;
                }
                try {
                    List<ReviewListItem> newReviews = ReviewController.getReviewListItems(lastID, getActivity());
                    Course.Reviews.addAll(newReviews);
                    for (ReviewListItem item : newReviews) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override

                            public void run() {
                                View appView = item.createView(inflater, container, savedInstanceState);
                                reviewListView.addView(appView);
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

class AsyncGetTask extends AsyncTask<FragmentActivity, Void, CourseRetrievalModel>{

    @Override
    protected CourseRetrievalModel doInBackground(FragmentActivity... fragmentActivities) {
        CourseRetrievalModel course = null;
        try{
            course = CourseController.getCourseById(1, fragmentActivities[0]);
        } catch(Exception ioe) {
            Log.e("ERR", "Couldn't get course" + ioe.toString());
        }
        return course;
    }
}


