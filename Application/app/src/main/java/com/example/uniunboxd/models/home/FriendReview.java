package com.example.uniunboxd.models.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.fragments.student.ReviewFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FriendReview implements View.OnClickListener {
    public final int Id;
    public final String CourseName;
    public final String CourseImage;
    public final int StudentId;
    public final String StudentName;
    public final String StudentImage;
    public final double Rating;
    private Fragment fragment;

    @JsonCreator
    public FriendReview(@JsonProperty("id") int id, @JsonProperty("courseName") String courseName,
                        @JsonProperty("courseImage") String courseImage, @JsonProperty("studentId") int studentId,
                        @JsonProperty("studentName") String studentName, @JsonProperty("studentImage") String studentImage,
                        @JsonProperty("rating") double rating) {
        Id = id;
        CourseName = courseName;
        CourseImage = courseImage;
        StudentId = studentId;
        StudentName = studentName;
        StudentImage = studentImage;
        Rating = rating;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.course_name_image_review_item, container, false);

        TextView courseName = view.findViewById(R.id.courseName);
        courseName.setOnClickListener(this);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        courseImage.setOnClickListener(this);
        TextView studentName = view.findViewById(R.id.studentName);
        studentName.setOnClickListener(this);
        ImageView studentImage = view.findViewById(R.id.studentImage);
        studentImage.setOnClickListener(this);
        RatingBar rating = view.findViewById(R.id.ratingBar);

        courseName.setText(CourseName);
        if (CourseImage != null) {
            courseImage.setImageBitmap(ImageHandler.decodeImageString(CourseImage));
        }
        studentName.setText(StudentName);
        if (StudentImage != null) {
            studentImage.setImageBitmap(ImageHandler.decodeImageString(StudentImage));
        }
        rating.setRating((float) Rating);

        fragment = f;

        return view;
    }

    @Override
    public void onClick(View v) {
        int test = v.getId();
        if (v.getId() == R.id.courseName || v.getId() == R.id.courseImage) {
            ((IActivity) fragment.getActivity()).replaceFragment(new ReviewFragment(Id), true);
        } else if (v.getId() == R.id.studentName || v.getId() == R.id.studentImage) {
            ((IActivity) fragment.getActivity()).replaceFragment(new ProfileFragment(StudentId), true);
        }
    }
}
