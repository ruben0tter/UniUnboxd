package com.example.uniunboxd.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceHolderNameAdvanced {
    public int Id;
    public String CourseImage;
    public String CourseName;
    public ReviewPoster Poster;
    public float Rating;

    @JsonCreator
    public PlaceHolderNameAdvanced(@JsonProperty("id") int id, @JsonProperty("courseImage") String courseImage, @JsonProperty("courseName") String courseName,
                                   @JsonProperty("poster") ReviewPoster poster, @JsonProperty("rating") float rating) {
        Id = id;
        CourseImage = courseImage;
        CourseName = courseName;
        Rating = rating;
        Poster = poster;
    }
    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState, PlaceHolderNameAdvanced placeHolderNameAdvanced) {

        View view = inflater.inflate(R.layout.course_name_image_review_item, container, false);
        ImageView courseImage = view.findViewById(R.id.popular_course_image);
        TextView courseName = view.findViewById(R.id.popular_course_text);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView posterName = view.findViewById(R.id.friend_name);
        ImageView posterIcon = view.findViewById(R.id.profile_picture);
        //TODO: set up the id

        courseName.setText(placeHolderNameAdvanced.CourseName);
        // courseImage.setImage or something idk
        if (Poster != null) {
            posterName.setText(placeHolderNameAdvanced.Poster.UserName);
            //posterIcon.setImage or something idk rn
        }
        ratingBar.setRating(placeHolderNameAdvanced.Rating);
        return view;
    }
}
