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
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CourseSearchResult class that represents a course search result.
 */
public class CourseSearchResult extends SearchResult {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String University;
    public final int UniversityId;
    public final String Image;
    public final String Professor;
    public final double AnonRating;
    public final double NonaRating;


    /**
     * Constructor for the CourseSearchResult class.
     *
     * @param id          The course's ID.
     * @param name        The course's name.
     * @param code        The course's code.
     * @param university  The course's university.
     * @param universityId The course's university ID.
     * @param professor   The course's professor.
     * @param image       The course's image.
     * @param anonRating  The course's anonymous rating.
     * @param nonaRating  The course's non-anonymous rating.
     */
    @JsonCreator
    public CourseSearchResult(@JsonProperty("id") int id,
                              @JsonProperty("name") String name,
                              @JsonProperty("code") String code,
                              @JsonProperty("university") String university,
                              @JsonProperty("universityId") int universityId,
                              @JsonProperty("professor") String professor,
                              @JsonProperty("image") String image,
                              @JsonProperty("anonymousRating") int anonRating,
                              @JsonProperty("nonanonymousRating") int nonaRating) {
        Id = id;
        Name = name;
        Code = code;
        University = university;
        UniversityId = universityId;
        Professor = professor;
        Image = image;
        AnonRating = anonRating;
        NonaRating = nonaRating;
    }


    /**
     * Creates a view for the course search result.
     *
     * @param inflater The layout inflater.
     * @param activity The activity.
     * @return The view for the course search result.
     */
    public View createView(LayoutInflater inflater, Activity activity) {
        View view = inflater.inflate(R.layout.search_result_course, null);

        // Set the on click listener for the view to redirect to the course fragment.
        view.setOnClickListener(l -> ((IActivity) activity).replaceFragment(new CourseFragment(Id), true));

        ((TextView) view.findViewById(R.id.course)).setText(Name);
        ((TextView) view.findViewById(R.id.code)).setText(Code);
        ((TextView) view.findViewById(R.id.university)).setText(University);
        ((RatingBar) view.findViewById(R.id.anonRating)).setRating((float) AnonRating);
        ((RatingBar) view.findViewById(R.id.nonAnonRating)).setRating((float) NonaRating);

        // Set the image if it exists.
        if (Image != null) {
            ImageView image = view.findViewById(R.id.image);

            byte[] decodedString = Base64.decode(Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }


        return view;
    }
}
