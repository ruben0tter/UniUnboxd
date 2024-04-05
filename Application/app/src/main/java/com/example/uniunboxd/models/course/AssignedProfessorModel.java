package com.example.uniunboxd.models.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.fragments.university.CreateCourseFragment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignedProfessorModel {
    public final int Id;
    public final String Name;
    public final String Email;

    /**
     * Constructor for the AssignedProfessorModel class.
     *
     * @param id    The professor's ID.
     * @param name  The professor's name.
     * @param email The professor's email.
     */
    @JsonCreator
    public AssignedProfessorModel(@JsonProperty("id") int id,
                                  @JsonProperty("name") String name,
                                  @JsonProperty("email") String email) {
        Id = id;
        Name = name;
        Email = email;
    }

    /**
     * Creates a view for the assigned professor.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The create course fragment.
     * @return The view for the assigned professor.
     */
    public View CreateView(LayoutInflater inflater, ViewGroup container, CreateCourseFragment f) {
        View view = inflater.inflate(R.layout.selected_professors_item, container, false);
        TextView name = view.findViewById(R.id.ProfName);
        TextView email = view.findViewById(R.id.ProfEmail);
        ImageButton unassignProfessor = view.findViewById(R.id.unassign_professor_button);

        name.setText(Name);
        email.setText(Email);
        AssignedProfessorModel model = this;
        
        // Unassign the professor from the course.
        unassignProfessor.setOnClickListener(v -> f.RemoveAssignedProfessor(model, view));
        return view;
    }
}
