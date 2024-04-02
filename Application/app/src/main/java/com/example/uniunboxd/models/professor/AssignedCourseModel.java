package com.example.uniunboxd.models.professor;

import android.os.Bundle;
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

public class AssignedCourseModel {
    public final int Id;
    public final double Rating;
    public final String Name;
    public final String Code;
    public final String Professor;
    public final String Image;

    @JsonCreator
    public AssignedCourseModel(@JsonProperty("id") int Id,
                               @JsonProperty("rating") double Rating,
                               @JsonProperty("name") String Name,
                               @JsonProperty("code") String Code,
                               @JsonProperty("professor") String Professor,
                               @JsonProperty("image") String Image) {
        this.Id = Id;
        this.Rating = Rating;
        this.Name = Name;
        this.Code = Code;
        this.Professor = Professor;
        this.Image = Image;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Fragment f) {
        View view = inflater.inflate(R.layout.course_simple_view_item, container, false);

        TextView courseName = view.findViewById(R.id.course_name);
        TextView courseCode = view.findViewById(R.id.course_code);
        TextView professorName = view.findViewById(R.id.professor_name);
        ImageView courseImage = view.findViewById(R.id.course_image);

        courseName.setText(Name);
        courseCode.setText(Code);
        professorName.setText("Professor: " + Professor);

        if (Image != null && !Image.equals("string"))
            courseImage.setImageBitmap(ImageHandler.decodeImageString(Image));
        else
            courseImage.setImageResource(R.drawable.app_logo);
        view.setOnClickListener(v -> {
            ((IActivity) f.getActivity()).replaceFragment(new CourseFragment(Id), true);
        });
        return view;
    }
}

