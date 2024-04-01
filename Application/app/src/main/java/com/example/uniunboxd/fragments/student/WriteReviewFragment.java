package com.example.uniunboxd.fragments.student;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.example.uniunboxd.utilities.ImageHandler;

import java.net.HttpURLConnection;

public class WriteReviewFragment extends Fragment implements View.OnClickListener {
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
        View view = inflater.inflate(R.layout.fragment_write_review, container, false);

        // Course Info
        TextView courseName = (TextView) view.findViewById(R.id.courseName);
        TextView courseCode = (TextView) view.findViewById(R.id.courseCode);
        ImageView courseImage = (ImageView) view.findViewById(R.id.courseImage);
        setCourseInfo(courseName, courseCode, courseImage);

        // Inputs
        comment = (EditText) view.findViewById(R.id.comment);
        rating = (RatingBar) view.findViewById(R.id.rating);
        isAnonymous = (CheckBox) view.findViewById(R.id.isAnonymous);

        // Buttons
        Button post = (Button) view.findViewById(R.id.postButton);
        post.setOnClickListener(this);
        Button delete = (Button) view.findViewById(R.id.searchButton);
        delete.setOnClickListener(this);

        if (review == null) {
            delete.setText("Cancel Review");
        } else {
            post.setText("Update Review");
            setReviewInfo();
        }

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

    private void setCourseInfo(TextView name, TextView code, ImageView image) {
        name.setText(course.name);
        code.setText(course.code);

        if (course.image != null) {
            image.setImageBitmap(ImageHandler.decodeImageString(course.image));
        }
    }

    private void setReviewInfo() {
        comment.setText(review.comment);
        rating.setRating((float) review.rating);
        isAnonymous.setChecked(review.isAnonymous);
    }

    private void postReview() {
        if (review == null) {
            ReviewModel model = createReviewModel();

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection response = ReviewController.postReview(model, getActivity());
                        if (response.getResponseCode() == 200) {
                            // TODO: Show notification with "Review successfully created."
                            ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
                        } else {
                            // TODO: Show notification with error message.
                        }
                    } catch (Exception e) {
                        Log.e("APP", "Failed to post review: " + e);
                    }

                }
            });
        } else {
            putReview();
        }
    }

    private void putReview() {
        updateReviewModel();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection response = ReviewController.putReview(review, getActivity());
                    if (response.getResponseCode() == 200) {
                        // TODO: Show notification with "Review successfully updated."
                        ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
                    } else {
                        // TODO: Show notification with error message.
                    }
                } catch (Exception e) {
                    Log.e("APP", "Failed to put review: " + e);
                }

            }
        });
    }

    private void deleteReview() {
        if (review == null) {
            ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
        } else {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection response = ReviewController.deleteReview(review.id, getActivity());
                        if (response.getResponseCode() == 200) {
                            // TODO: Show notification with "Review successfully deleted."
                            ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
                        } else {
                            // TODO: Show notification with error message.
                        }
                    } catch (Exception e) {
                        Log.e("APP", "Failed to delete review: " + e);
                    }

                }
            });
        }
    }

    private ReviewModel createReviewModel() {
        return new ReviewModel(
                rating.getRating(),
                comment.getText().toString(),
                isAnonymous.isChecked(),
                course.id);
    }

    private void updateReviewModel() {
        review.rating = rating.getRating();
        review.comment = comment.getText().toString();
        review.isAnonymous = isAnonymous.isChecked();

    }
}
