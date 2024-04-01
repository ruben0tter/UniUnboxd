package com.example.uniunboxd.models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.CourseFragment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseSearchResult {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String University;
    public final int UniversityId;
    public final String Image;
    public final String Professor;
    public final double AverageRating;

    @JsonCreator
    public CourseSearchResult(@JsonProperty("id") int id,
                              @JsonProperty("name") String name,
                              @JsonProperty("code") String code,
                              @JsonProperty("university") String university,
                              @JsonProperty("universityId") int universityId,
                              @JsonProperty("professor") String professor,
                              @JsonProperty("image") String image,
                              @JsonProperty("averageRating") int averageRating) {
        Id = id;
        Name = name;
        Code = code;
        University = university;
        UniversityId = universityId;
        Professor = professor;
        Image = image;
        AverageRating = averageRating;
    }


    public View createView(LayoutInflater inflater, Activity activity) {
        View view = inflater.inflate(R.layout.search_result_course, null);

        view.setOnClickListener(l -> {
            ((IActivity) activity).replaceFragment(new CourseFragment(Id), true);
        });

        ((TextView) view.findViewById(R.id.course)).setText(Name);
        ((TextView) view.findViewById(R.id.code)).setText(Code);
        ((TextView) view.findViewById(R.id.university)).setText(University);
        ((RatingBar) view.findViewById(R.id.anonRating)).setRating((float) AverageRating);
        ((RatingBar) view.findViewById(R.id.nonAnonRating)).setRating((float) AverageRating);

        if (Image != null) {
            ImageView image = view.findViewById(R.id.image);

            byte[] decodedString = Base64.decode(Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }


        return view;
    }
}
