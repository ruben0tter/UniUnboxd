package com.example.uniunboxd.fragments.professor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.models.professor.ProfessorEditModel;
import com.example.uniunboxd.models.professor.ProfessorProfileModel;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.concurrent.ExecutionException;

/**
 * ProfessorProfileFragment class that represents the professor profile screen.
 */
public class ProfessorProfileFragment extends Fragment {
    private int ID;
    private final int NUM_COURSES_TO_LOAD = 5;
    protected ProfessorProfileModel Professor;

    /**
     * Necessary empty constructor.
     */
    public ProfessorProfileFragment() {
    }

    /**
     * Constructor for the ProfessorProfileFragment class.
     *
     * @param id The professor's ID.
     */
    public ProfessorProfileFragment(int id) {
        ID = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Creates the view for the professor profile fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the professor profile fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (ID == 0)
            return null;

        View view;

        // Get professor information.
        GetProfessorInformationAsyncTask asyncGetTask = new GetProfessorInformationAsyncTask(ID, NUM_COURSES_TO_LOAD);
        try {
            Professor = asyncGetTask.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (Professor == null) {
            return null;
        }

        view = Professor.createView(inflater, container, this);

        ImageButton editBtn = view.findViewById(R.id.editButton);
        TextView signOutBtn = view.findViewById(R.id.signOut);

        // Set the click listeners for the edit and sign out buttons.
        editBtn.setOnClickListener(v -> {
            ProfessorEditModel professorEditModel = MakeProfessorEditModel(Professor);
            // Replace the fragment with the professor edit fragment.
            ((IActivity) getActivity()).replaceFragment(new ProfessorEditFragment(professorEditModel), true);
        });
        signOutBtn.setOnClickListener(v -> {
            JWTValidation.deleteToken(getActivity());
            // Replace the activity with the main activity.
            ((IActivity) getActivity()).replaceActivity(MainActivity.class);
        });
        int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
        if (userId != ID) {
            editBtn.setVisibility(View.GONE);
            signOutBtn.setVisibility(View.GONE);
        }
        return view;
    }

    private ProfessorEditModel MakeProfessorEditModel(ProfessorProfileModel professor) {
        return new ProfessorEditModel(professor.Id, professor.Image, professor.Name);
    }
}

// AsyncTask class to get the professor information.
class GetProfessorInformationAsyncTask extends AsyncTask<FragmentActivity, Void, ProfessorProfileModel> {

    private final int ID;
    private final int NUM_OF_REVIEWS_TO_LOAD;

    public GetProfessorInformationAsyncTask(int id, int numCoursesToLoad) {
        ID = id;
        NUM_OF_REVIEWS_TO_LOAD = numCoursesToLoad;
    }

    // Get the professor information.
    @Override
    protected ProfessorProfileModel doInBackground(FragmentActivity... fragments) {
        ProfessorProfileModel professor = null;
        try {
            professor = UserController.getProfessorProfile(ID, fragments[0]);
        } catch (Exception ioe) {
            Log.e("ERR", "Couldn't get course" + ioe);
        }
        return professor;
    }
}
