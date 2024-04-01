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
import java.net.HttpURLConnection;

public class ProfessorEditFragment extends Fragment {
    private ProfessorEditModel Professor;
    public ProfessorEditFragment(ProfessorEditModel professor){
       Professor = professor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = Professor.createView(inflater, container, savedInstanceState);

        TextView name = v.findViewById(R.id.professorNameEdit);
        ImageButton editImageBtn = v.findViewById(R.id.professorImageEditButton);
        Button saveChangesBtn = v.findViewById(R.id.saveChanges);

        Fragment f = this;
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Professor.Name = name.getText().toString();
                        try{
                            HttpURLConnection response = UserController.putProfessor(Professor, getActivity());
                            if (response.getResponseCode() == 200) {
                                // TODO: Show notification with "Review successfully created."
                                ((IActivity) getActivity()).replaceFragment(new ProfessorProfileFragment(Professor.Id), true);
                            } else {
                                // TODO: Show notification with error message.
                            }
                        } catch (Exception e) {
                            Log.e("APP", "Failed to post review: " + e);
                        }
                    }
                });
            }
        });
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSystemChooser.ChooseImage(f, 1);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = this.getView().findViewById(R.id.professorImage);

        Uri uri = data.getData();

        byte[] bitmapdata = null;
        try {
            bitmapdata = FileSystemChooser.readTextFromUri(uri, getActivity());
        } catch (IOException e) {
            Log.e("ERR", e.toString());
        }
        Professor.Image = Base64.encodeToString(bitmapdata, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        image.setImageBitmap(bitmap);
    }

}
