package com.example.uniunboxd.models.professor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The AssignedCourseModel class is a model for an assigned course.
 */
public class AssignedCourseModel {
    public final int Id;
    public final float AnonymousRating;
    public final float NonAnonymousRating;
    public final String Name;
    public final String Code;
    public final String University;
    public final String Image;

    /**
     * Constructor for the AssignedCourseModel class.
     *
     * @param Id               The course's ID.
     * @param AnonymousRating  The course's anonymous rating.
     * @param NonAnonymousRating The course's non-anonymous rating.
     * @param Name             The course's name.
     * @param Code             The course's code.
     * @param University       The course's university.
     * @param Image            The course's image.
     */
    @JsonCreator
    public AssignedCourseModel(@JsonProperty("id") int Id,
                               @JsonProperty("anonymousRating") float AnonymousRating,
                               @JsonProperty("nonanonymousRating") float NonAnonymousRating,
                               @JsonProperty("name") String Name,
                               @JsonProperty("code") String Code,
                               @JsonProperty("university") String University,
                               @JsonProperty("image") String Image) {
        this.Id = Id;
        this.AnonymousRating = AnonymousRating;
        this.NonAnonymousRating = NonAnonymousRating;
        this.Name = Name;
        this.Code = Code;
        this.University = University;
        this.Image = Image;
    }

    /**
     * Creates a view for the assigned course.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The fragment.
     * @return The view for the assigned course.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.search_result_course, container, false);

        TextView courseName = view.findViewById(R.id.course);
        TextView courseCode = view.findViewById(R.id.code);
        TextView university = view.findViewById(R.id.university);
        ImageView courseImage = view.findViewById(R.id.image);
        RatingBar anonRating = view.findViewById(R.id.anonRating);
        RatingBar nonAnonRating = view.findViewById(R.id.nonAnonRating);

        courseName.setText(Name);
        courseCode.setText(Code);
        university.setText(University);

        // Set the image if it exists else set the default image.
        if (Image != null && !Image.equals("string"))
            courseImage.setImageBitmap(ImageHandler.decodeImageString(Image));
        else
            courseImage.setImageResource(R.drawable.app_logo);

        view.setOnClickListener(v -> ((IActivity) f.getActivity()).replaceFragment(new CourseFragment(Id), true));
        return view;
    }
}

