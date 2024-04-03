package com.example.uniunboxd.models.course;

import android.os.Bundle;
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

    @JsonCreator
    public AssignedProfessorModel(@JsonProperty("id") int id,
                                  @JsonProperty("name") String name,
                                  @JsonProperty("email") String email) {
        Id = id;
        Name = name;
        Email = email;
    }

    public View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, CreateCourseFragment f) {
        View view = inflater.inflate(R.layout.selected_professors_item, container, false);
        TextView name = view.findViewById(R.id.ProfName);
        TextView email = view.findViewById(R.id.ProfEmail);
        ImageButton unassignProfessor = view.findViewById(R.id.unassign_professor_button);

        name.setText(Name);
        email.setText(Email);
        AssignedProfessorModel model = this;
        unassignProfessor.setOnClickListener(v -> f.RemoveAssignedProfessor(model, view));
        return view;
    }
}
