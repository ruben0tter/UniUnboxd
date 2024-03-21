package com.example.uniunboxd.models;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfessorProfileModel {
    public final int Id;
    public final String Image;
    public final String Name;

    @JsonCreator
    public ProfessorProfileModel(@JsonProperty("id") int id, @JsonProperty("profilePic") String image, @JsonProperty("name") String name) {
        Id = id;
        Image = image;
        Name = name;
        //TODO: Add University
        //TODO: Add assigned Courses
    }

    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page_professor, container, false);
        TextView name = view.findViewById(R.id.professorName);
        ImageView image = view.findViewById(R.id.professorImage);

        name.setText(Name);
        if(Image != null && !Image.equals("")){
            Bitmap imageBitmap = ImageHandler.decodeImageString(Image);
            image.setImageBitmap(imageBitmap);
        }
        //TODO: inflate LinearLayout

        //TODO: add courses to the LinearLayout
        return view;
    }
}
