package com.example.uniunboxd.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.uniunboxd.R;
import com.example.uniunboxd.models.Home.PopularCoursesModel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class PlaceHolderNameMaster {
    public final Collection<PopularCoursesModel> BasicCourses;
    public final Collection<PlaceHolderNameAdvanced> AdvancedCourses;


    @JsonCreator
    public PlaceHolderNameMaster(@JsonProperty("basicCourses") Collection<PopularCoursesModel> basicCourses, @JsonProperty("advancedCourses") Collection<PlaceHolderNameAdvanced> advancedCourses) {
        BasicCourses = basicCourses;
        AdvancedCourses = advancedCourses;
    }

    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);

        for(PopularCoursesModel i : BasicCourses) {
            linearLayout.addView(i.createView(inflater, container, savedInstanceState, i));
        }
        for(PlaceHolderNameAdvanced i : AdvancedCourses) {
            linearLayout2.addView(i.createView(inflater, container, savedInstanceState, i));
        }
        for(PlaceHolderNameAdvanced i : AdvancedCourses) {
            linearLayout3.addView(i.createView(inflater, container, savedInstanceState, i));
        }

        return view;
    }
}
