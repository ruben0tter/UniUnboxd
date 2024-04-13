package com.example.uniunboxd.models.student;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The StudentListItem class is a model for a student list item.
 */
public class StudentListItem {
    public final int ID;
    public final String Name;
    public final String Image;

    /**
     * Constructor for the StudentListItem class.
     *
     * @param id    The student's ID.
     * @param name  The student's name.
     * @param image The student's image.
     */
    @JsonCreator
    public StudentListItem(@JsonProperty("id") int id, @JsonProperty("name") String name,
                           @JsonProperty("image") String image) {
        ID = id;
        Name = name;
        Image = image;
    }

    /**
     * Creates a view for the student list item.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param a         The activity.
     * @return The view for the student list item.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Activity a) {
        View view = inflater.inflate(R.layout.user_icon_name_item, container, false);
        ImageView image = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);

        // Set the student's image and name.
        if (Image != null) {
            image.setImageBitmap(ImageHandler.decodeImageString(Image));
        }

        name.setText(Name);

        // Set the on click listener to go to the student's profile.
        image.setOnClickListener(v -> ((IActivity) a).replaceFragment(new StudentProfileFragment(ID), true));

        return view;
    }
}
