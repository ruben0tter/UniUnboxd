package com.example.uniunboxd.fragments.professor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.professor.ProfessorEditModel;
import com.example.uniunboxd.utilities.FileSystemChooser;

import java.io.IOException;

/**
 * ProfessorEditFragment class that represents the professor edit screen.
 */
public class ProfessorEditFragment extends Fragment {
    private ProfessorEditModel Professor;

    // Necessary empty constructor.
    public ProfessorEditFragment() {
    }

    // Constructor for the ProfessorEditFragment class.
    public ProfessorEditFragment(ProfessorEditModel professor) {
        Professor = professor;
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Creates the view for the professor edit fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the professor edit fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Professor == null)
            return null;

        View v = Professor.createView(inflater, container);

        TextView name = v.findViewById(R.id.professorNameEdit);
        ImageButton editImageBtn = v.findViewById(R.id.professorImageEditButton);
        Button saveChangesBtn = v.findViewById(R.id.saveChanges);

        Fragment f = this;
        saveChangesBtn.setOnClickListener(v1 -> AsyncTask.execute(() -> {
            Professor.Name = name.getText().toString();
            try {
                // Update the professor with the new information.
                UserController.putProfessor(Professor, getActivity());
                // Replace the fragment with the professor's profile.
                ((IActivity) getActivity()).replaceFragment(new ProfessorProfileFragment(Professor.Id), true);
            } catch (Exception e) {
                Log.e("APP", "Failed to post review: " + e);
            }
        }));
        // Set the on click listener to choose an image.
        editImageBtn.setOnClickListener(v12 -> FileSystemChooser.ChooseImage(f, 1));

        return v;
    }

    /**
     * Handles the result of the file system chooser.
     *
     * @param requestCode The request code.
     * @param resultCode  The result code.
     * @param data        The data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = this.getView().findViewById(R.id.professorImage);

        Uri uri = data.getData();

        byte[] bitmapdata = null;
        try {
            // Read the image from the URI.
            bitmapdata = FileSystemChooser.readTextFromUri(uri, getActivity());
        } catch (IOException e) {
            Log.e("ERR", e.toString());
        }
        Professor.Image = Base64.encodeToString(bitmapdata, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        image.setImageBitmap(bitmap);
    }
}
