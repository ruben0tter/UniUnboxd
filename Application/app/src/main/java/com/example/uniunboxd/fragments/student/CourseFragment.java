package com.example.uniunboxd.fragments.student;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.models.CourseRetrievalModel;

import java.util.concurrent.ExecutionException;

public class CourseFragment extends Fragment {

    private CourseRetrievalModel Course = null;

    public CourseFragment(int id) {
        // TODO: Get course information
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
        AsyncGetTask asyncGetTask = new AsyncGetTask();
        try {
            Course = asyncGetTask.execute(getActivity()).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(Course != null) {
            view = Course.createView(inflater, container, savedInstanceState);
        }
        return view;
    }
}

class AsyncGetTask extends AsyncTask<FragmentActivity, Void, CourseRetrievalModel>{

    @Override
    protected CourseRetrievalModel doInBackground(FragmentActivity... fragmentActivities) {
        CourseRetrievalModel course = null;
        try{
            course = CourseController.getCourseById(1, fragmentActivities[0]);
        } catch(Exception ioe) {
            Log.e("ERR", "Couldn't get course" + ioe.toString());
        }
        return course;
    }
}


