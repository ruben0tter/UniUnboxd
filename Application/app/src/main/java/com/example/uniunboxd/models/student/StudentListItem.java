package com.example.uniunboxd.models.student;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StudentListItem {
    public final int ID;
    public final String Name;
    public final String Image;

    @JsonCreator
    public StudentListItem(@JsonProperty("id") int id, @JsonProperty("name") String name,
                           @JsonProperty("image") String image) {
        ID = id;
        Name = name;
        Image = image;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Activity a){
        View view = inflater.inflate(R.layout.user_icon_name_item, container, false);
        ImageView image = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);

        if(Image != null) {
            image.setImageBitmap(ImageHandler.decodeImageString(Image));
        }

        name.setText(Name);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActivity) a).replaceFragment(new StudentProfileFragment(ID), true);
            }
        });

        return view;
    }
}
