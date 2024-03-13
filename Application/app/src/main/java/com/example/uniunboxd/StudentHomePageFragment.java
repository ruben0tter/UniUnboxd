package com.example.uniunboxd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.models.PlaceHolderNameMaster;

import java.util.concurrent.ExecutionException;

public class StudentHomePageFragment extends Fragment {

    private PlaceHolderNameMaster Home = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = null;
        AsyncGetTaskNew asyncGetTask = new AsyncGetTaskNew();
        try {
            Home = asyncGetTask.execute().get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(Home != null) {
            view = Home.createView(inflater, container, savedInstanceState);
        }
        return view;
    }

}

class AsyncGetTaskNew extends AsyncTask<Void, Void, PlaceHolderNameMaster> {
    @Override
    protected PlaceHolderNameMaster doInBackground(Void... voids) {
        PlaceHolderNameMaster home = null;
        try{
            //home = CourseController.getCourseById(1);
        } catch(Exception ioe) {
            Log.e("ERR", "Couldn't get course" + ioe.toString());
        }
        return home;
    }
}
