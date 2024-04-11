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

/**
 * StudentProfileFragment class that represents the student profile screen.
 */
public class StudentProfileFragment extends Fragment {

    private int ID;
    private StudentProfileModel Student;

    /**
     * Necessary empty constructor.
     */
    public StudentProfileFragment() {
    }

    /**
     * Constructor for the StudentProfileFragment class.
     *
     * @param id The student's ID.
     */
    public StudentProfileFragment(int id) {
        ID = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the student profile fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the student profile fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = null;
        GetStudentInformationAsyncTask asyncGetTask = new GetStudentInformationAsyncTask(ID, 7);
        try {
            // Get the student information.
            Student = asyncGetTask.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If the student is null, return the view.
        if (Student == null) {
            return view;
        }

        // Create the view.
        view = Student.createView(inflater, container, this);
        // Edit button.
        ImageButton editBtn = view.findViewById(R.id.editButton);

        // Edit button.
        editBtn.setOnClickListener(v -> {
            StudentEditModel studentEditModel = new StudentEditModel(Student);
            ((IActivity) getActivity()).replaceFragment(new StudentEditFragment(studentEditModel), true);
        });
        int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
        if (userId != ID) {
            // If the user is not the owner of the profile, hide the edit button.
            editBtn.setVisibility(View.GONE);
        }

        // Hide the following and followers sections if there are no users you follow or followers.
        if (Student.Following == null || Student.Following.size() == 0) {
            view.findViewById(R.id.Following).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowing).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowingLinear).setVisibility(View.GONE);
        }
        if (Student.Followers == null || Student.Followers.size() == 0) {
            view.findViewById(R.id.Followers).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowers).setVisibility(View.GONE);
            view.findViewById(R.id.listFollowersLinear).setVisibility(View.GONE);
        }
        return view;
    }
}

/**
 * GetStudentInformationAsyncTask class that represents the async task to get student information.
 */
class GetStudentInformationAsyncTask extends AsyncTask<FragmentActivity, Void, StudentProfileModel> {

    private final int ID;
    private final int NUM_OF_REVIEWS_TO_LOAD;

    /**
     * Constructor for the GetStudentInformationAsyncTask class.
     *
     * @param id                The student's ID.
     * @param numCoursesToLoad The number of courses to load.
     */
    public GetStudentInformationAsyncTask(int id, int numCoursesToLoad) {
        ID = id;
        NUM_OF_REVIEWS_TO_LOAD = numCoursesToLoad;
    }

    /**
     * Gets the student information in the background.
     *
     * @param fragments The fragments.
     * @return The student information.
     */
    @Override
    protected StudentProfileModel doInBackground(FragmentActivity... fragments) {
        StudentProfileModel student = null;
        try {
            // Get the student information.
            student = UserController.getStudent(ID, fragments[0]);
        } catch (Exception ioe) {
            Log.e("ERR", "Couldn't get course" + ioe);
        }
        return student;
    }
}