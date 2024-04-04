package com.example.uniunboxd.fragments.student;

import android.app.AlertDialog;
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

public class WriteReviewFragment extends Fragment implements View.OnClickListener {
    private EditText comment;
    private RatingBar rating;
    private CheckBox isAnonymous;
    private CourseModel course;
    private ReviewModel review;

    public WriteReviewFragment() {
    }

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
        if (course == null)
            return null;

        View view = inflater.inflate(R.layout.fragment_write_review, container, false);

        // Course Info
        TextView courseName = view.findViewById(R.id.courseName);
        TextView courseCode = view.findViewById(R.id.courseCode);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        setCourseInfo(courseName, courseCode, courseImage);

        // Inputs
        comment = view.findViewById(R.id.comment);
        rating = view.findViewById(R.id.rating);
        isAnonymous = view.findViewById(R.id.isAnonymous);

        // Buttons
        Button post = view.findViewById(R.id.postButton);
        post.setOnClickListener(this);
        Button delete = view.findViewById(R.id.searchButton);
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

            AsyncTask.execute(() -> {
                try {
                    ReviewController.postReview(model, getActivity());
                    ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
                } catch (Exception e) {
                    Log.e("APP", "Failed to post review: " + e);
                }

            });
        } else {
            putReview();
        }
    }

    private void putReview() {
        updateReviewModel();

        AsyncTask.execute(() -> {
            try {
                ReviewController.putReview(review, getActivity());
                ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
            } catch (Exception e) {
                Log.e("APP", "Failed to put review: " + e);
            }

        });
    }

    private void deleteReview() {
        if (review == null) {
            ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.FlagReview);
            builder.setTitle("Are you sure you want to delete the review?");

            builder.setPositiveButton("Yes", (dialog, which) -> AsyncTask.execute(() -> {
                try {
                    ReviewController.deleteReview(review.id, getActivity());
                    ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id), true);
                } catch (Exception e) {
                    Log.e("APP", "Failed to delete review: " + e);
                }

            }));
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            builder.show();
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
