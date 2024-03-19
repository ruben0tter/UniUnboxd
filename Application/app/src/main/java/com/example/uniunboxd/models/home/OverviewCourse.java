package com.example.uniunboxd.models.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.CourseFragment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OverviewCourse implements View.OnClickListener {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Professor;
    public final String Image;
    private Fragment fragment;

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
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        // TODO: Add view creation of course overview.
        return null;
    }
    @Override
    public void onClick(View v) {
        ((IActivity) fragment.getActivity()).replaceFragment(new CourseFragment(Id));
    }
}
