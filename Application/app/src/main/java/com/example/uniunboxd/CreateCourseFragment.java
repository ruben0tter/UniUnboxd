package com.example.uniunboxd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.models.CourseCreationModel;

import java.net.HttpURLConnection;

public class CreateCourseFragment extends Fragment implements View.OnClickListener{
    public CreateCourseFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_page_setup, container, false);
        Button btn = (Button) view.findViewById(R.id.saveChanges);
        btn.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.saveChanges) {
            //TODO: fix these ID's
            //TODO: Add image/banner
            ConstraintLayout layout = (ConstraintLayout) view.getParent();
            EditText name = (EditText) layout.getViewById(R.id.courseName_courseName_edit);
            EditText code = (EditText) layout.getViewById(R.id.courseName_courseCode_edit);
            EditText description = (EditText) layout.getViewById(R.id.courseDescription_edit);
            EditText professor = (EditText) layout.getViewById(R.id.courseName_courseDescription_edit);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    //TODO: get university id from JWT
                    CourseCreationModel course = new CourseCreationModel(name.getText().toString(),
                            code.getText().toString(), description.getText().toString(),
                            professor.getText().toString(), null, null, 1);
                    try{
                        HttpURLConnection con = CourseController.postCourse(course);
                        if(con.getResponseCode() == 200){
                            replaceFragment(new HomeFragment());
                        }
                        else{
                            //TODO: see how to show a toast
                        }
                    } catch (Exception e) {
                        Log.e("ERR", e.toString());
                    }

                }
            });
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
