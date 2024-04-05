package com.example.uniunboxd.models.professor;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfessorEditModel {
    public int Id;
    public String Image;
    public String Name;

    @JsonCreator
    public ProfessorEditModel(@JsonProperty("id") int id, @JsonProperty("profilePic") String image, @JsonProperty("name") String name) {
        Id = id;
        Image = image;
        Name = name;
        //TODO: Add University
        //TODO: Add assigned Courses
    }

    public View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_profile_page_professor_edit, container, false);
        TextView name = view.findViewById(R.id.professorNameEdit);
        ImageView image = view.findViewById(R.id.professorImage);

        name.setText(Name);
        if (Image != null && !Image.equals("")) {
            Bitmap imageBitmap = ImageHandler.decodeImageString(Image);
            image.setImageBitmap(imageBitmap);
        }

        return view;
    }
}
