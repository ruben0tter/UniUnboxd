package com.example.uniunboxd.models.student;

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
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentReviewListItem {
    public final int ID;

    public final float Rating;

    public final String Comment;

    public final StudentReviewCourse Course;

    /**
     * Constructor for the StudentReviewListItem class.
     *
     * @param ID      The review's ID.
     * @param Rating  The review's rating.
     * @param Comment The review's comment.
     * @param course  The review's course.
     */
    @JsonCreator
    public StudentReviewListItem(@JsonProperty("id") int ID, @JsonProperty("rating") float Rating,
                                 @JsonProperty("comment") String Comment, @JsonProperty("studentProfileReviewCourse") StudentReviewCourse course) {
        this.ID = ID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.Course = course;
    }

    /**
     * Creates a view for the review list item.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The fragment.
     * @return The view for the review list item.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.profile_review_item, container, false);
        RatingBar rating = view.findViewById(R.id.ratingBar);
        TextView courseCode = view.findViewById(R.id.courseCode);
        TextView courseName = view.findViewById(R.id.courseName);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        TextView comment = view.findViewById(R.id.comment);

        rating.setRating(Rating);
        courseCode.setText(Course.Code);
        courseName.setText(Course.Name);

        // Set the course's image.
        if (Course.Image != null)
            courseImage.setImageBitmap(ImageHandler.decodeImageString(Course.Image));
        comment.setText(Comment);

        // Set the on click listener to go to the review.
        comment.setOnClickListener(v -> ((IActivity) f.getActivity()).replaceFragment(new ReviewFragment(ID), true));
        return view;
    }
}
