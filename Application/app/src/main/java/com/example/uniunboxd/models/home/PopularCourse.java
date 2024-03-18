package com.example.uniunboxd.models.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.CourseFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PopularCourse implements View.OnClickListener {
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

        TextView courseName = view.findViewById(R.id.courseName);
        courseName.setOnClickListener(this);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        courseImage.setOnClickListener(this);

        courseName.setText(CourseName);
        if (CourseImage != null) {
            courseImage.setImageBitmap(ImageHandler.decodeImageString(CourseImage));
        }

        fragment = f;

        return view;
    }
    @Override
    public void onClick(View v) {
        ((IActivity) fragment.getActivity()).replaceFragment(new CourseFragment(Id));
    }
}
