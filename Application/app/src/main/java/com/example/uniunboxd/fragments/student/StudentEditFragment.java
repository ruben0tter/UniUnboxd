package com.example.uniunboxd.fragments.student;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.CourseCreationModel;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.ImageHandler;
import com.example.uniunboxd.utilities.JWTValidation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

public class StudentEditFragment extends Fragment {

    private StudentEditModel Model;

    public StudentEditFragment(StudentEditModel studentEditModel) {
        Model = studentEditModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student_profile_page_edit, container, false);

        ImageView image = view.findViewById(R.id.image);
        EditText name = view.findViewById(R.id.name);
        ImageView editImageBtn = view.findViewById(R.id.editImageBtn);
        Button saveChangesBtn = view.findViewById(R.id.saveChanges);

        name.setText(Model.Name);

        if(Model.Image != null)
            image.setImageBitmap(ImageHandler.decodeImageString(Model.Image));

        Fragment f = this;
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSystemChooser.ChooseImage(f);
            }
        });

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Model.Name = name.getText().toString();
                            HttpURLConnection con = UserController.putStudent(Model, getActivity());
                            if(con.getResponseCode() == 200){
                                ((IActivity) getActivity()).replaceFragment(new StudentProfileFragment(Model.Id));
                            }
                            else{
                                //TODO: see how to show a toast
                                Log.d("DEB", ""+con.getResponseCode());
                            }
                        } catch (Exception e) {
                            Log.e("ERR", e.toString());
                        }

                    }
                });
            }
        });

        //TODO: set Notification settings
        //TODO: set verification
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = this.getView().findViewById(R.id.image);

        Uri uri = data.getData();

        byte[] bitmapdata = null;
        try {
            bitmapdata = FileSystemChooser.readTextFromUri(uri, getActivity());
        } catch (IOException e) {
            Log.e("ERR", e.toString());
        }
        Model.Image = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        image.setImageBitmap(bitmap);
    }
}
