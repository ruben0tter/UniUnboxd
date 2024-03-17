package com.example.uniunboxd.models.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Review {
    public final int Id;
    public final Date Date;
    public final double Rating;
    public final String Comment;
    public final boolean IsAnonymous;
    public final CourseHeader Course;
    public final UserHeader Student;
    public final List<Reply> Replies;

    @JsonCreator
    public Review(@JsonProperty("id") int id, @JsonProperty("date") Date date,
                  @JsonProperty("rating") double rating, @JsonProperty("comment") String comment,
                  @JsonProperty("isAnonymous") boolean isAnonymous, @JsonProperty("courseHeader") CourseHeader course,
                  @JsonProperty("studentHeader") UserHeader student, @JsonProperty("replies") List<Reply> replies) {
        Id = id;
        Date = date;
        Rating = rating;
        Comment = comment;
        IsAnonymous = isAnonymous;
        Course = course;
        Student = student;
        Replies = replies;
    }

    public void createView(View view, LayoutInflater inflater, ViewGroup container, Fragment f) {
        TextView reviewHeader = view.findViewById(R.id.reviewHeader);
        ImageView courseBanner = view.findViewById(R.id.courseBanner);
        ImageView profileImage = view.findViewById(R.id.profileImage);
        TextView profileName = view.findViewById(R.id.profileName);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        TextView courseName = view.findViewById(R.id.courseName);
        RatingBar rating = view.findViewById(R.id.rating);
        TextView reviewDate = view.findViewById(R.id.reviewDate);
        TextView reviewComment = view.findViewById(R.id.reviewComment);

        if (IsAnonymous) {
            reviewHeader.setText("Anonymous review");
            profileName.setText("Unknown");
        } else {
            reviewHeader.setText(String.format("%s's review", Student.Name));
            if (Student.Image != null) {
                profileImage.setImageBitmap((ImageHandler.decodeImageString(Student.Image)));
                profileImage.setBackground(null);
            }
            profileName.setText(Student.Name);
        }

        if (Course.Banner != null) {
            courseBanner.setImageBitmap(ImageHandler.decodeImageString(Course.Banner));
            courseBanner.setBackground(null);
        }
        if (Course.Image != null) {
            courseImage.setImageBitmap(ImageHandler.decodeImageString(Course.Image));
            courseImage.setBackground(null);
        }
        courseName.setText(Course.Name);
        rating.setRating((float) Rating);
        reviewDate.setText(String.format("Reviewed on %s", new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(Date)));
        reviewComment.setText(Comment);

        LinearLayout replies = view.findViewById(R.id.replies);

        for(int i = 0; i < Replies.size(); i++) {
            Reply r = Replies.get(i);

            if(i == Replies.size() - 1) {
                // Don't add reply divider on last reply.
                replies.addView(r.createView(inflater, container, f, true));
            } else {
                replies.addView(r.createView(inflater, container, f, false));
            }
        }
    }
}
