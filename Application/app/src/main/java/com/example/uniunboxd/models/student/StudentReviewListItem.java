package com.example.uniunboxd.models.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentReviewListItem {
    public final int ID;

    public final float Rating;

    public final String Comment;

    public final StudentReviewCourse Course;
    @JsonCreator
    public StudentReviewListItem(@JsonProperty("id") int ID, @JsonProperty("rating") float Rating,
                                 @JsonProperty("comment") String Comment, @JsonProperty("studentProfileReviewCourse") StudentReviewCourse course) {
        this.ID = ID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.Course = course;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_review_item, container, false);
        RatingBar rating = view.findViewById(R.id.ratingBar);
        TextView courseCode = view.findViewById(R.id.courseCode);
        TextView courseName = view.findViewById(R.id.courseName);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        TextView comment = view.findViewById(R.id.comment);

        rating.setRating(Rating);
        courseCode.setText(Course.Code);
        courseName.setText(Course.Name);
        if(Course.Image != null)
            courseImage.setImageBitmap(ImageHandler.decodeImageString(Course.Image));
        comment.setText(Comment);

        return view;
    }
}
