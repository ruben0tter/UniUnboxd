package com.example.uniunboxd.fragments.professor;

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
import com.example.uniunboxd.models.ProfessorEditModel;
import com.example.uniunboxd.models.ProfessorProfileModel;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.concurrent.ExecutionException;

public class ProfessorProfileFragment extends Fragment {
    private final int ID;
    private final int NUM_COURSES_TO_LOAD = 5;
    protected ProfessorProfileModel Professor;

    public ProfessorProfileFragment(int id) {
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
        GetProfessorInformationAsyncTask asyncGetTask = new GetProfessorInformationAsyncTask(ID, NUM_COURSES_TO_LOAD);
        try {
            Professor = asyncGetTask.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(Professor != null) {
            view = Professor.createView(inflater, container, savedInstanceState);
        }

        ImageButton editBtn = view.findViewById(R.id.editButton);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessorEditModel professorEditModel = MakeProfessorEditModel(Professor);
                ((IActivity) getActivity()).replaceFragment(new ProfessorEditFragment(professorEditModel));
            }
        });
        int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
        if(userId != ID) {
            editBtn.setVisibility(View.GONE);
        }
        return view;
    }

    private ProfessorEditModel MakeProfessorEditModel(ProfessorProfileModel professor) {
        return new ProfessorEditModel(professor.Id, professor.Image, professor.Name);
    }
}


class GetProfessorInformationAsyncTask extends AsyncTask<FragmentActivity, Void, ProfessorProfileModel>{

    private final int ID;
    private final int NUM_OF_REVIEWS_TO_LOAD;

    public GetProfessorInformationAsyncTask(int id, int numCoursesToLoad) {
        ID = id;
        NUM_OF_REVIEWS_TO_LOAD = numCoursesToLoad;
    }

    @Override
    protected ProfessorProfileModel doInBackground(FragmentActivity... fragments) {
        ProfessorProfileModel professor = null;
        try{
            professor = UserController.getProfessorProfile(ID, fragments[0]);
        } catch(Exception ioe) {
            Log.e("ERR", "Couldn't get course" + ioe.toString());
        }
        return professor;
    }
}
