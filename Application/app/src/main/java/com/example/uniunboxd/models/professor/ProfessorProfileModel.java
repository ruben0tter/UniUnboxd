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

        if (Image != null && !Image.equals("")) {
            Bitmap imageBitmap = ImageHandler.decodeImageString(Image);
            image.setImageBitmap(imageBitmap);
        } else {
            image.setImageResource(R.drawable.app_logo);
        }
        LinearLayout listAssignedCourses = view.findViewById(R.id.listAssignedCourses);

        if (AssignedCourses == null || AssignedCourses.isEmpty()) {
            universityName.setVisibility(View.GONE);
            ConstraintLayout coursesHeaderWrapper = view.findViewById(R.id.coursesHeaderWrapper);
            coursesHeaderWrapper.setVisibility(View.GONE);
        } else {
            for (AssignedCourseModel x : AssignedCourses) {
                listAssignedCourses.addView(x.createView(inflater, container, f));
            }
        }
        return view;
    }
}
