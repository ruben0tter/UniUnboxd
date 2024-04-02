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

public class OverviewCourse implements View.OnClickListener {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Professor;
    public final String Image;
    private Fragment fragment;

    @JsonCreator
    public OverviewCourse(@JsonProperty("id") int id, @JsonProperty("name") String name,
                          @JsonProperty("code") String code, @JsonProperty("professor") String professor,
                          @JsonProperty("image") String image) {
        Id = id;
        Name = name;
        Code = code;
        Professor = professor;
        Image = image;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.fragment_uni_home_page_courses, container, false);

        TextView courseName = view.findViewById(R.id.courseName_text);
        courseName.setOnClickListener(this);
        TextView courseCode = view.findViewById(R.id.courseCode_text);
        courseName.setOnClickListener(this);
        TextView professorText = view.findViewById(R.id.professor_text);
        courseName.setOnClickListener(this);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        courseImage.setOnClickListener(this);

        courseName.setText("Course: " + Name);
        courseCode.setText("Code: " + Code);
        professorText.setText("Professor: " + Professor);
        if (Image != null) {
            courseImage.setImageBitmap(ImageHandler.decodeImageString(Image));
        }

        fragment = f;
        return view;

    }

    @Override
    public void onClick(View v) {
        ((IActivity) fragment.getActivity()).replaceFragment(new CourseFragment(Id), true);
    }
}
