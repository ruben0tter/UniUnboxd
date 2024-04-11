package com.example.uniunboxd.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * ReviewListItem class that represents a review list item.
 */
public class ReviewListItem {
    public int Id;
    public float Rating;
    public String Comment;
    public boolean IsAnonymous;
    public int CourseId;
    public ReviewPoster Poster;

    /**
     * Constructor for the ReviewListItem class.
     *
     * @param id          The review's ID.
     * @param rating      The review's rating.
     * @param comment     The review's comment.
     * @param isAnonymous Whether the review is anonymous.
     * @param courseId    The review's course ID.
     * @param poster      The review's poster.
     */
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

    /**
     * Creates a view for the review list item.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @return The view for the review list item.
     */
    public View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.review_list_item, container, false);
        TextView comment = view.findViewById(R.id.ReviewListItem_Comment);
        RatingBar ratingBar = view.findViewById(R.id.RatingBar);
        TextView posterName = view.findViewById(R.id.ReviewListItem_PosterName);
        ImageView posterIcon = view.findViewById(R.id.ReviewListItem_PosterIcon);

        comment.setText(Comment);

        // Set the poster's name and icon of the review.
        if (Poster != null) {
            posterName.setText(Poster.UserName);
            if (Poster.Image != null && !Poster.Image.equals("")) {
                byte[] iconData = Base64.decode(Poster.Image, Base64.DEFAULT);
                Bitmap bannerBitmap = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
                posterIcon.setImageBitmap(bannerBitmap);
            }
        }
        ratingBar.setRating(Rating);

        // Get the role of the user from the JWT token.
        String role = JWTValidation.getTokenProperty(view.getContext(), "typ");

        // Set the on click listener for the view to redirect to the review fragment.
        comment.setOnClickListener(v -> ((IActivity) v.getContext()).replaceFragment(new ReviewFragment(Id), true));
        if (role.equals("University"))
            comment.setClickable(false);
        return view;
    }

}
