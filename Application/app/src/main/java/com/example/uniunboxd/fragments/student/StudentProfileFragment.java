package com.example.uniunboxd.fragments.student;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.models.student.StudentProfileModel;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.concurrent.ExecutionException;

public class StudentProfileFragment extends Fragment {

    private final int ID;
    private StudentProfileModel Student;
    public StudentProfileFragment(int id) {
        ID = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = null;
        GetStudentInformationAsyncTask asyncGetTask = new GetStudentInformationAsyncTask(ID, 7);
        try {
            Student = asyncGetTask.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(Student != null) {
            view = Student.createView(inflater, container, savedInstanceState, this);
        }

        ImageButton editBtn = view.findViewById(R.id.editButton);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentEditModel studentEditModel = new StudentEditModel(Student);
                ((IActivity) getActivity()).replaceFragment(new StudentEditFragment(studentEditModel));
            }
        });
        int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
        if(userId != ID) {
            editBtn.setVisibility(View.GONE);
        }
        return view;
    }
}
class GetStudentInformationAsyncTask extends AsyncTask<FragmentActivity, Void, StudentProfileModel> {

    private final int ID;
    private final int NUM_OF_REVIEWS_TO_LOAD;

    public GetStudentInformationAsyncTask(int id, int numCoursesToLoad) {
        ID = id;
        NUM_OF_REVIEWS_TO_LOAD = numCoursesToLoad;
    }

    @Override
    protected StudentProfileModel doInBackground(FragmentActivity... fragments) {
        StudentProfileModel student = null;
        try{
            student = UserController.getStudent(ID, fragments[0]);
        } catch(Exception ioe) {
            Log.e("ERR", "Couldn't get course" + ioe.toString());
        }
        return student;
    }
}