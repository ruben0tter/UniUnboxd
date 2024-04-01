package com.example.uniunboxd.models.course;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.models.ReviewListItem;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CourseRetrievalModel {
    public final int Id;
    public final String Name;
    public final String Code;
    public final float AnonymousRating;
    public final float NonanonymousRating;
    public final String Description;
    public final String Professor;
    public final String Image;
    public final String Banner;
    public final int UniversityId;
    public final String UniversityName;
    public final List<ReviewListItem> Reviews;
    public final List<Integer> AssignedProfessors;

    @JsonCreator
    public CourseRetrievalModel(@JsonProperty("id") int id, @JsonProperty("name")String name,
                                @JsonProperty("code") String code, @JsonProperty("anonymousRating") float anonymousRating,
                                @JsonProperty("nonanonymousRating") float nonanonymousRating, @JsonProperty("description") String description,
                                @JsonProperty("professor") String professor, @JsonProperty("image") String image,
                                @JsonProperty("banner") String banner, @JsonProperty("universityId") int universityId,
                                @JsonProperty("reviews") List<ReviewListItem> reviews, @JsonProperty("universityName") String universityName,
                                @JsonProperty("assignedProfessors") List<Integer> assignedProfessors) {
        Id = id;
        Name = name;
        Code = code;
        AnonymousRating = anonymousRating;
        NonanonymousRating = nonanonymousRating;
        Description = description;
        Professor = professor;
        Image = image;
        Banner = banner;
        UniversityId = universityId;
        Reviews = reviews;
        UniversityName = universityName;
        AssignedProfessors = assignedProfessors;
    }

    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        TextView name = view.findViewById(R.id.courseName);
        TextView code = view.findViewById(R.id.courseCode);
        TextView professor = view.findViewById(R.id.professor);
        TextView description = view.findViewById(R.id.description);
        TextView universityName = view.findViewById(R.id.universityName);
        ImageView image = view.findViewById(R.id.courseImage);
        ImageView banner = view.findViewById(R.id.courseBanner);
        RatingBar anonRatingBar = view.findViewById(R.id.ratingBarAnonymous);
        RatingBar nonanonRatingBar = view.findViewById(R.id.ratingBarNonAnonymous);
        TextView anonRatingNum = view.findViewById(R.id.anonymous_rating_number);
        TextView nonanonRatingNum = view.findViewById(R.id.non_anonymous_rating_number);

        anonRatingBar.setRating(AnonymousRating);
        nonanonRatingBar.setRating(NonanonymousRating);
        anonRatingNum.setText(""+AnonymousRating);
        nonanonRatingNum.setText(""+NonanonymousRating);
        name.setText(Name);
        code.setText(Code);
        professor.setText(Professor);
        description.setText(Description);
        universityName.setText(UniversityName);
        if(Image != null && !Image.equals("")){
            Bitmap imageBitmap = ImageHandler.decodeImageString(Image);
            image.setImageBitmap(imageBitmap);
        }
        if(Banner != null && !Banner.equals("")) {
            Bitmap bannerBitmap = ImageHandler.decodeImageString(Banner);
            banner.setImageBitmap(bannerBitmap);
        }
        LinearLayout linearLayout = view.findViewById(R.id.reviewList);

        for(ReviewListItem i : Reviews) {
            linearLayout.addView(i.createView(inflater, container, savedInstanceState));
        }

        return view;
    }
}
