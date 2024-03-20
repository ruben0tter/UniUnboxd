package com.example.uniunboxd.models;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.ReviewFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ReviewListItem {
    public int Id;
    public float Rating;
    public String Comment;
    public boolean IsAnonymous;
    public int CourseId;
    public ReviewPoster Poster;

    @JsonCreator
    public ReviewListItem(@JsonProperty("id") int id, @JsonProperty("rating") float rating, @JsonProperty("comment") String comment,
                          @JsonProperty("isAnonymous") boolean isAnonymous, @JsonProperty("courseId") int courseId,
                          @JsonProperty("poster") ReviewPoster poster) {
        Id = id;
        Rating = rating;
        Comment = comment;
        IsAnonymous = isAnonymous;
        CourseId = courseId;
        Poster = poster;
    }
    public View createView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_list_item, container, false);
        TextView comment = view.findViewById(R.id.ReviewListItem_Comment);
        RatingBar ratingBar = view.findViewById(R.id.ReviewListItem_RatingBar);
        TextView posterName = view.findViewById(R.id.ReviewListItem_PosterName);
        ImageView posterIcon = view.findViewById(R.id.ReviewListItem_PosterIcon);
        
        comment.setText(Comment);
        if(Poster != null) {
            posterName.setText(Poster.UserName);
            if(Poster.Image != null && !Poster.Image.equals("")) {
                byte[] iconData = Base64.decode(Poster.Image, Base64.DEFAULT);
                Bitmap bannerBitmap = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
                posterIcon.setImageBitmap(bannerBitmap);
            }
        }
        ratingBar.setRating(Rating);
        String role = JWTValidation.getTokenProperty(view.getContext(), "typ");
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActivity) v.getContext()).replaceFragment(new ReviewFragment(Id));
            }
        });
        if(role.equals("University"))
            comment.setClickable(false);
        return view;
    }

}
