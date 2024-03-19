package com.example.uniunboxd.fragments.university;

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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.R;
import com.example.uniunboxd.models.CourseCreationModel;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.JWTValidation;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.FileSystem;

public class CreateCourseFragment extends Fragment implements View.OnClickListener{
    private final int imageCode = 1;
    private final int bannerCode = 2;

    private String imageEnc = null;
    private String bannerEnc = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_page_setup, container, false);

        Button btn = view.findViewById(R.id.saveChanges);
        ImageButton imageBtn = view.findViewById(R.id.courseImage_edit);
        ImageButton bannerBtn = view.findViewById(R.id.courseBanner_edit);

        btn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        bannerBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment f = FragmentManager.findFragment(view);
        int id = view.getId();
        if(id == R.id.saveChanges) {
            //TODO: fix these ID's
            ConstraintLayout layout = (ConstraintLayout) view.getParent();
            EditText name = (EditText) layout.getViewById(R.id.courseName_courseName_edit);
            EditText code = (EditText) layout.getViewById(R.id.courseName_courseCode_edit);
            EditText description = (EditText) layout.getViewById(R.id.courseDescription_edit);
            EditText professor = (EditText) layout.getViewById(R.id.courseName_courseDescription_edit);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int universityId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(),"sub"));
                    CourseCreationModel course = new CourseCreationModel(name.getText().toString(),
                            code.getText().toString(), description.getText().toString(),
                            professor.getText().toString(), imageEnc, bannerEnc, universityId);
                    try{
                        HttpURLConnection con = CourseController.postCourse(course, getActivity());
                        if(con.getResponseCode() == 200){
                            replaceFragment(new HomeFragment());
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
        else if(id == R.id.courseImage_edit) {
            FileSystemChooser.ChooseImage(f, imageCode);
        }
        else if(id == R.id.courseBanner_edit) {
            FileSystemChooser.ChooseImage(f, bannerCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = null;
        if(requestCode == imageCode) {
            image = this.getView().findViewById(R.id.courseImage_image_courseSetup);
        }
        else if (requestCode == bannerCode) {
            image = this.getView().findViewById(R.id.courseBanner_image_courseSetup);
        }
        Uri uri = data.getData();

        byte[] bitmapdata = null;
        try {
            bitmapdata = FileSystemChooser.readTextFromUri(uri, getActivity());
        } catch (IOException e) {
            Log.e("ERR", e.toString());
        }

        encodeImageOrBanner(bitmapdata, requestCode);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        image.setImageBitmap(bitmap);
    }

    public void encodeImageOrBanner(byte[] data, int code) {
        if(code == imageCode){
            imageEnc = Base64.encodeToString(data, Base64.DEFAULT);
        } else if (code == bannerCode) {
            bannerEnc = Base64.encodeToString(data, Base64.DEFAULT);
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
