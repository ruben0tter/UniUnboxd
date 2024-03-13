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

public class PlaceHolderNameBasic {
    public int Id;
    public String CourseImage;
    public String CourseName;

    @JsonCreator
    public PlaceHolderNameBasic(@JsonProperty("id") int id, @JsonProperty("courseImage") String courseImage, @JsonProperty("courseName") String courseName) {
        Id = id;
        CourseImage = courseImage;
        CourseName = courseName;
    }
    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState, PlaceHolderNameBasic placeHolderNameBasic) {

        View view = inflater.inflate(R.layout.course_name_image_item, container, false);
        ImageView courseImage = view.findViewById(R.id.popular_course_image);
        TextView courseName = view.findViewById(R.id.popular_course_text);
        //TODO: set up the id

        courseName.setText(placeHolderNameBasic.CourseName);
        //Set the image idk how to do that rn
        //courseImage.(placeHolderNameBasic.CourseImage);
        return view;
    }

}
