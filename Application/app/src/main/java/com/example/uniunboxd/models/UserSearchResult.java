package com.example.uniunboxd.models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserSearchResult class that represents a user search result.
 */
public class UserSearchResult extends SearchResult {
    public final int Id;
    public final String UserName;
    public final String Image;
    public final int UserType;

    /**
     * Constructor for the UserSearchResult class.
     *
     * @param id      The user's ID.
     * @param username The user's username.
     * @param image    The user's image.
     * @param userType The user's type.
     */
    @JsonCreator
    public UserSearchResult(@JsonProperty("id") int id,
                            @JsonProperty("userName") String username,
                            @JsonProperty("image") String image,
                            @JsonProperty("userType") int userType) {
        Id = id;
        UserName = username;
        Image = image;
        UserType = userType;
    }

    // The types of users.
    private static final String[] USER_TYPES = new String[]{
            "Student",
            "University",
            "Professor"
    };

    /**
     * Creates a view for the user search result.
     *
     * @param inflater The layout inflater.
     * @param activity The activity.
     * @return The view for the user search result.
     */
    public View createView(LayoutInflater inflater, Activity activity) {
        View view = inflater.inflate(R.layout.search_result_user, null);

        // Set the on click listener to go to the user's profile.
        view.setOnClickListener(v -> ((IActivity) activity).replaceFragment(new StudentProfileFragment(Id), true));

        ((TextView) view.findViewById(R.id.username)).setText(UserName);
        ((TextView) view.findViewById(R.id.role)).setText(USER_TYPES[UserType]);

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
