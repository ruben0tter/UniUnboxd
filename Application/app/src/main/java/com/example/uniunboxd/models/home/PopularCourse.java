package com.example.uniunboxd.models.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PopularCourse {
    public final int Id;
    public final String CourseImage;
    public final String CourseName;
    private Fragment fragment;

    @JsonCreator
    public PopularCourse(@JsonProperty("id") int id,
                         @JsonProperty("image") String courseImage,
                         @JsonProperty("name") String courseName) {
        Id = id;
        CourseImage = courseImage;
        CourseName = courseName;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.course_name_image_item, container, false);
        view.setOnClickListener(l -> ((IActivity) fragment.getActivity()).replaceFragment(new CourseFragment(Id), true));

        TextView courseName = view.findViewById(R.id.courseName);
        ImageView courseImage = view.findViewById(R.id.courseImage);

        courseName.setText(CourseName);
        if (CourseImage != null) {
            courseImage.setImageBitmap(ImageHandler.decodeImageString(CourseImage));
        }

        fragment = f;

        return view;
    }
}
