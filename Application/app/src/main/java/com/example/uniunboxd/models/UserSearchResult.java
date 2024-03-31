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
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSearchResult {
    public final int Id;
    public final String UserName;
    public final String Image;
    public final String University;
    public final int UserType;

    @JsonCreator
    public UserSearchResult(@JsonProperty("id") int id,
                            @JsonProperty("userName") String username,
                            @JsonProperty("image") String image,
                            @JsonProperty("userType") int userType,
                            @JsonProperty("university") String university) {
        Id = id;
        UserName = username;
        Image = image;
        UserType = userType;
        University = university;
    }

    private static String[] USER_TYPES = new String[]{
            "Student",
            "University",
            "Professor"
    };

    public View createView(LayoutInflater inflater, Activity activity) {
        View view = inflater.inflate(R.layout.search_result_user, null);

        view.setOnClickListener(l -> {
            ((IActivity) activity).replaceFragment(new ProfileFragment(Id));
        });

        ((TextView) view.findViewById(R.id.username)).setText(UserName);
        ((TextView) view.findViewById(R.id.role)).setText(USER_TYPES[UserType]);
        ((TextView) view.findViewById(R.id.university)).setText(University);

        if (Image != null) {
            ImageView image = view.findViewById(R.id.image);

            byte[] decodedString = Base64.decode(Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }


        return view;
    }
}