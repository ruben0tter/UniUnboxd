package com.example.uniunboxd.models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.uniunboxd.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseSearchResult {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String University;
    public final int UniversityId;

    public final String Image;
    public final double AverageRating;

    @JsonCreator
    public CourseSearchResult(@JsonProperty("id") int id, @JsonProperty("name") String name,
                              @JsonProperty("code") String code, @JsonProperty("image") String image,
                              @JsonProperty("university") String university,
                              @JsonProperty("universityId") int universityId,
                              @JsonProperty("averageRating") int averageRating) {
        Id = id;
        Name = name;
        Code = code;
        Image = image;
        University = university;
        UniversityId = universityId;
        AverageRating = averageRating;
    }


    public View createView(LayoutInflater inflater, LinearLayout parent, Activity activity) {
        View view = inflater.inflate(R.layout.search_result_course, null);


        ImageView image = view.findViewById(R.id.imageView);

        byte[] decodedString = Base64.decode(Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);

        return view;
    }
}
