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

        if(Student == null) {
            Log.e("ERR", "Something went wrong.");
        }

        view = Student.createView(inflater, container, savedInstanceState, this);
        ImageButton editBtn = view.findViewById(R.id.editButton);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentEditModel studentEditModel = new StudentEditModel(Student);
                ((IActivity) getActivity()).replaceFragment(new StudentEditFragment(studentEditModel), true);
            }
        });
        int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
        if(userId != ID) {
            editBtn.setVisibility(View.GONE);
        }

        if(Student.Following == null || Student.Following.size() == 0) {
            view.findViewById(R.id.Following).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowing).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowingLinear).setVisibility(View.GONE);
        }
        if(Student.Followers == null || Student.Followers.size() == 0) {
            view.findViewById(R.id.Followers).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowers).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowersLinear).setVisibility(View.GONE);
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