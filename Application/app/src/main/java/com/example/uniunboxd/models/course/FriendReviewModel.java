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

/**
 * FriendReviewModel class that represents a friend review model.
 */
public class FriendReviewModel implements View.OnClickListener {
    public int Id;
    public double Rating;
    public String Name;
    public String Image;
    public boolean HasComment;

    private FragmentActivity activity;

    /**
     * Constructor for the FriendReviewModel class.
     *
     * @param Id         The review's ID.
     * @param Rating     The review's rating.
     * @param Name       The review's name.
     * @param Image      The review's image.
     * @param HasComment Whether the review has a comment.
     */
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

    /**
     * Creates a view for the friend review model.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The fragment activity.
     * @return The view for the friend review model.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, FragmentActivity f) {
        View view = inflater.inflate(R.layout.friends_course_page_item, container, false);

        // Set the image, name, and rating.с
        ShapeableImageView image = view.findViewById(R.id.image);
        image.setOnClickListener(this);

        // Set the comment icon if the review has a comment.
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

        // Set the image, name, and rating of the review.
        if(Image != null) {
            image.setImageBitmap(ImageHandler.decodeImageString(Image));
        }
        name.setText(Name);
        rating.setRating((float) Rating);

        activity = f;

        return view;
    }

    /**
     * Handles the click event for the friend review model.
     *
     * @param v The view.
     */
    @Override
    public void onClick(View v) {
        // Go to the review fragment.
        ((IActivity) activity).replaceFragment(new ReviewFragment(Id), true);
    }
}
