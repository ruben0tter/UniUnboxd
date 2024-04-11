package com.example.uniunboxd.models.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OverviewCourse class that represents an overview course.
 */
public class OverviewCourse {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Professor;
    public final String Image;
    private Fragment fragment;

    /**
     * Constructor for the OverviewCourse class.
     *
     * @param id        The course's ID.
     * @param name      The course's name.
     * @param code      The course's code.
     * @param professor The course's professor.
     * @param image     The course's image.
     */
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

    /**
     * Creates a view for the overview course.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The fragment.
     * @return The view for the overview course.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.fragment_uni_home_page_courses, container, false);
        view.setOnClickListener(l -> ((IActivity) fragment.getActivity()).replaceFragment(new CourseFragment(Id), true));

        TextView courseName = view.findViewById(R.id.courseName_text);
        TextView courseCode = view.findViewById(R.id.courseCode_text);
        TextView professorText = view.findViewById(R.id.professor_text);
        ImageView courseImage = view.findViewById(R.id.courseImage);

        courseName.setText("Course: " + Name);
        courseCode.setText("Code: " + Code);
        professorText.setText("Professor: " + Professor);
        if (Image != null) {
            courseImage.setImageBitmap(ImageHandler.decodeImageString(Image));
        }

        fragment = f;
        return view;
    }
}
