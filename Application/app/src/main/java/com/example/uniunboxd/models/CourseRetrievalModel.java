package com.example.uniunboxd.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.List;

public class CourseRetrievalModel {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Description;
    public final String Professor;

    public final String Image;
    public final String Banner;

    public final int UniversityId;
    public final String UniversityName;
    public final List<ReviewListItem> Reviews;

    @JsonCreator
    public CourseRetrievalModel(@JsonProperty("id") int id, @JsonProperty("name")String name,
                                @JsonProperty("code") String code, @JsonProperty("description") String description,
                                @JsonProperty("professor") String professor, @JsonProperty("image") String image,
                                @JsonProperty("banner") String banner, @JsonProperty("universityId") int universityId,
                                @JsonProperty("reviews") List<ReviewListItem> reviews, @JsonProperty("universityName") String universityName) {
        Id = id;
        Name = name;
        Code = code;
        Description = description;
        Professor = professor;
        Image = image;
        Banner = banner;
        UniversityId = universityId;
        Reviews = reviews;
        UniversityName = universityName;
    }

    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_univerisity, container, false);
        TextView name = view.findViewById(R.id.courseName);
        TextView code = view.findViewById(R.id.courseCode);
        TextView professor = view.findViewById(R.id.professor);
        TextView description = view.findViewById(R.id.description);
        TextView universityName = view.findViewById(R.id.universityName);
        ImageView image = view.findViewById(R.id.courseImage);
        ImageView banner = view.findViewById(R.id.courseBanner);

        name.setText(Name);
        code.setText(Code);
        professor.setText(Professor);
        description.setText(Description);
        universityName.setText(UniversityName);
        if(Image != null && !Image.equals("")){
            byte[] imageData = Base64.decode(Image, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            image.setImageBitmap(imageBitmap);
        }
        if(Banner != null && !Banner.equals("")) {
            byte[] bannerData = Base64.decode(Banner, Base64.DEFAULT);
            Bitmap bannerBitmap = BitmapFactory.decodeByteArray(bannerData, 0, bannerData.length);
            image.setImageBitmap(bannerBitmap);
        }
        LinearLayout linearLayout = view.findViewById(R.id.reviewList);

        for(ReviewListItem i : Reviews) {
            linearLayout.addView(i.createView(inflater, container, savedInstanceState));
        }

        return view;
    }
}
