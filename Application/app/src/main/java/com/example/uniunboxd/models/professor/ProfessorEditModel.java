package com.example.uniunboxd.models.professor;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The ProfessorEditModel class is a model for a professor edit.
 */
public class ProfessorEditModel {
    public int Id;
    public String Image;
    public String Name;

    /**
     * Constructor for the ProfessorEditModel class.
     *
     * @param id    The professor's ID.
     * @param image The professor's image.
     * @param name  The professor's name.
     */
    @JsonCreator
    public ProfessorEditModel(@JsonProperty("id") int id, @JsonProperty("profilePic") String image, @JsonProperty("name") String name) {
        Id = id;
        Image = image;
        Name = name;
    }

    /**
     * Creates a view for the professor edit model.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @return The view for the professor edit model.
     */
    public View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_profile_page_professor_edit, container, false);
        TextView name = view.findViewById(R.id.professorNameEdit);
        ImageView image = view.findViewById(R.id.professorImage);

        name.setText(Name);

        // Set the professor's image if it exists.
        if (Image != null && !Image.equals("")) {
            Bitmap imageBitmap = ImageHandler.decodeImageString(Image);
            image.setImageBitmap(imageBitmap);
        }

        return view;
    }
}
