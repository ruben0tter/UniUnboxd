package com.example.uniunboxd.fragments.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.utilities.JWTValidation;

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
        View view =  inflater.inflate(R.layout.fragment_write_review, container, false);

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
        Button delete = (Button) view.findViewById(R.id.deleteButton);
        delete.setOnClickListener(this);

        if (review == null) {
            delete.setText("Cancel Review");
        } else {
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
            byte[] decodedString = Base64.decode(course.image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }
    }

    private void setReviewInfo() {
        comment.setText(review.comment);
        rating.setRating((float) review.rating);
        isAnonymous.setChecked(review.isAnonymous);
    }

    private void postReview() {
        if (review == null) {
            int studentId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));

            ReviewModel model = createReviewModel(studentId);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection response = ReviewController.postReview(model, getActivity());
                        if (response.getResponseCode() == 200) {
                            // TODO: Show notification with "Review succesfully created."
                            ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id));
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
            ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.id));
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
