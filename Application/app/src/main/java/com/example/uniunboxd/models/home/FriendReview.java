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
import com.example.uniunboxd.fragments.student.ReviewFragment;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FriendReview class that represents a friend review.
 */
public class FriendReview implements View.OnClickListener{
    public final int Id;
    public final String CourseName;
    public final String CourseImage;
    public final int StudentId;
    public final String StudentName;
    public final String StudentImage;
    public final double Rating;
    private Fragment fragment;

    /**
     * Constructor for the FriendReview class.
     *
     * @param id          The review's ID.
     * @param courseName  The review's course name.
     * @param courseImage The review's course image.
     * @param studentId   The review's student ID.
     * @param studentName The review's student name.
     * @param studentImage The review's student image.
     * @param rating      The review's rating.
     */
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

    /**
     * Creates a view for the friend review.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The fragment.
     * @return The view for the friend review.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.course_name_image_review_item, container, false);
        view.setOnClickListener(v -> {
            if (v.getId() == R.id.courseName || v.getId() == R.id.courseImage) {
                ((IActivity) fragment.getActivity()).replaceFragment(new ReviewFragment(Id), true);
            } else if (v.getId() == R.id.studentName || v.getId() == R.id.studentImage) {
                ((IActivity) fragment.getActivity()).replaceFragment(new StudentProfileFragment(StudentId), true);
            }
        });

        TextView courseName = view.findViewById(R.id.courseName);
        courseName.setOnClickListener(this);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        courseImage.setOnClickListener(this);
        TextView studentName = view.findViewById(R.id.studentName);
        studentName.setOnClickListener(this);
        ImageView studentImage = view.findViewById(R.id.studentImage);
        studentImage.setOnClickListener(this);
        RatingBar rating = view.findViewById(R.id.ratingBar);
        rating.setOnClickListener(this);

        courseName.setText(CourseName);

        // Set the course's image and the student's name and image if they exist.
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
        ((IActivity) fragment.getActivity()).replaceFragment(new ReviewFragment(Id), true);
    }
}
