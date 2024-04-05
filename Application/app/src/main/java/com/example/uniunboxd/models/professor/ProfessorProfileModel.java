package com.example.uniunboxd.models.professor;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProfessorProfileModel {
    public final int Id;
    public final String Image;
    public final String Name;
    public final String UniversityName;
    public final List<AssignedCourseModel> AssignedCourses;

    /**
     * Constructor for the ProfessorProfileModel class.
     *
     * @param id             The professor's ID.
     * @param image          The professor's image.
     * @param name           The professor's name.
     * @param universityName The professor's university name.
     * @param assignedCourses The professor's assigned courses.
     */
    @JsonCreator
    public ProfessorProfileModel(@JsonProperty("id") int id, @JsonProperty("profilePic") String image,
                                 @JsonProperty("name") String name, @JsonProperty("universityName") String universityName,
                                 @JsonProperty("courses") List<AssignedCourseModel> assignedCourses) {
        Id = id;
        Image = image;
        Name = name;
        UniversityName = universityName;
        AssignedCourses = assignedCourses;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.fragment_profile_page_professor, container, false);
        TextView name = view.findViewById(R.id.professorName);
        ImageView image = view.findViewById(R.id.professorImage);
        TextView universityName = view.findViewById(R.id.universityName);

        name.setText(Name);
        universityName.setText("Works at: " + UniversityName);

        // Set the professor's image if it exists, else set the default image.
        if (Image != null && !Image.equals("")) {
            Bitmap imageBitmap = ImageHandler.decodeImageString(Image);
            image.setImageBitmap(imageBitmap);
        } else {
            image.setImageResource(R.drawable.app_logo);
        }
        LinearLayout listAssignedCourses = view.findViewById(R.id.listAssignedCourses);

        // If the professor has no assigned courses, hide the university name and the courses header.
        if (AssignedCourses == null || AssignedCourses.isEmpty()) {
            universityName.setVisibility(View.GONE);
            ConstraintLayout coursesHeaderWrapper = view.findViewById(R.id.coursesHeaderWrapper);

            // Hide the courses header.
            coursesHeaderWrapper.setVisibility(View.GONE);
        } else {
            // Add the assigned courses to the view.
            for (AssignedCourseModel x : AssignedCourses) {
                listAssignedCourses.addView(x.createView(inflater, container, f));
            }
        }
        return view;
    }
}
