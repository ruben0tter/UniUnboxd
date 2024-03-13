package com.example.uniunboxd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.utilities.JWTValidation;

import java.net.HttpURLConnection;

public class WriteReviewFragment extends Fragment implements View.OnClickListener{
    private EditText comment;
    private RatingBar rating;
    private CheckBox isAnonymous;
    private final CourseModel course;
    private ReviewModel review;

    public WriteReviewFragment(CourseModel course) {
        this.course = course;
    }

    public WriteReviewFragment(CourseModel course, ReviewModel review) {
        this.course = course;
        this.review = review;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_write_review, container, false);

        // Inputs
        comment = (EditText) view.findViewById(R.id.comment);
        rating = (RatingBar) view.findViewById(R.id.rating);
        isAnonymous = (CheckBox) view.findViewById(R.id.isAnonymous);

        // Buttons
        Button sign_in = (Button) view.findViewById(R.id.postButton);
        sign_in.setOnClickListener(this);
        Button sign_up = (Button) view.findViewById(R.id.deleteButton);
        sign_up.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.postButton) {
            postReview();
        } else {
            deleteReview();
        }
    }

    private void postReview() {
        if (review == null) {
            int studentId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));

            ReviewModel model = createReviewModel(studentId);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection response = ReviewController.postReview(model);
                        if (response.getResponseCode() == 200) {
                            // TODO: Show notification with "Review succesfully created."
                            // TODO: Redirect to CourseFragment.
                        } else {
                            // TODO: Show notification with error message.
                        }
                    } catch (Exception e) {
                        Log.e("APP", "Failed to post review: " + e.toString());
                    }

                }
            });
        } else {
            putReview();
        }
    }

    private void putReview() {
        //TODO: Implement updating review functionality.
    }

    private void deleteReview() {
        if (review == null) {
            //TODO: Redirect to Course Fragment.
        } else {
            //TODO: Implement deleting review functionality.
        }
    }

    private ReviewModel createReviewModel(int studentId) {
        return new ReviewModel(
                rating.getRating(),
                comment.getText().toString(),
                isAnonymous.isChecked(),
                course.id,
                studentId);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
