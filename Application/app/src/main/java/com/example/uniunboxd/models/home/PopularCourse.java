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

/**
 * PopularCourse class that represents a popular course.
 */
public class PopularCourse {
    public final int Id;
    public final String CourseImage;
    public final String CourseName;
    private Fragment fragment;

    /**
     * Constructor for the PopularCourse class.
     *
     * @param id          The course's ID.
     * @param courseImage The course's image.
     * @param courseName  The course's name.
     */
    @JsonCreator
    public PopularCourse(@JsonProperty("id") int id,
                         @JsonProperty("image") String courseImage,
                         @JsonProperty("name") String courseName) {
        Id = id;
        CourseImage = courseImage;
        CourseName = courseName;
    }

    /**
     * Creates a view for the popular course.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The fragment.
     * @return The view for the popular course.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.course_name_image_item, container, false);
        view.setOnClickListener(l -> ((IActivity) fragment.getActivity()).replaceFragment(new CourseFragment(Id), true));

        TextView courseName = view.findViewById(R.id.courseName);
        ImageView courseImage = view.findViewById(R.id.courseImage);

        courseName.setText(CourseName);

        // Set the course image if it exists.
        if (CourseImage != null) {
            courseImage.setImageBitmap(ImageHandler.decodeImageString(CourseImage));
        }

        fragment = f;

        return view;
    }
}
