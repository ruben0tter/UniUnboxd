package com.example.uniunboxd.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
                                    Bundle savedInstanceState, ReviewListItem reviewListItem) {
        View view = inflater.inflate(R.layout.review_list_item, container, false);
        TextView comment = view.findViewById(R.id.ReviewListItem_Comment);
        RatingBar ratingBar = view.findViewById(R.id.ReviewListItem_RatingBar);
        TextView posterName = view.findViewById(R.id.ReviewListItem_PosterName);
        ImageView posterIcon = view.findViewById(R.id.ReviewListItem_PosterIcon);
        //TODO: set up the id

        comment.setText(reviewListItem.Comment);
        if(Poster != null) posterName.setText(reviewListItem.Poster.UserName);
        ratingBar.setRating(reviewListItem.Rating);
        return view;
    }

}
