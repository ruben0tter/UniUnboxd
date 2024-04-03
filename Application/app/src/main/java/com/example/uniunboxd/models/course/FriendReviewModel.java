package com.example.uniunboxd.models.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.ReviewFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.material.imageview.ShapeableImageView;

public class FriendReviewModel implements View.OnClickListener {
    public int Id;
    public double Rating;
    public String Name;
    public String Image;
    public boolean HasComment;

    private FragmentActivity activity;

    @JsonCreator
    public FriendReviewModel(@JsonProperty("id") int Id, @JsonProperty("rating") double Rating,
                             @JsonProperty("name") String Name, @JsonProperty("image") String Image,
                             @JsonProperty("hasComment") boolean HasComment) {
        this.Id = Id;
        this.Rating = Rating;
        this.Name = Name;
        this.Image = Image;
        this.HasComment = HasComment;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, FragmentActivity f) {
        View view = inflater.inflate(R.layout.friends_course_page_item, container, false);

        ShapeableImageView image = view.findViewById(R.id.image);
        image.setOnClickListener(this);
        ImageView hasComment = view.findViewById(R.id.hasComment);
        if (HasComment) {
            hasComment.setVisibility(View.VISIBLE);
            hasComment.setOnClickListener(this);
        } else {
            hasComment.setVisibility(View.GONE);
        }
        TextView name = view.findViewById(R.id.name);
        name.setOnClickListener(this);
        RatingBar rating = view.findViewById(R.id.rating);
        rating.setOnClickListener(this);

        if(Image != null) {
            image.setImageBitmap(ImageHandler.decodeImageString(Image));
        }
        name.setText(Name);
        rating.setRating((float) Rating);

        activity = f;

        return view;
    }

    @Override
    public void onClick(View v) {
        ((IActivity) activity).replaceFragment(new ReviewFragment(Id), true);
    }
}
