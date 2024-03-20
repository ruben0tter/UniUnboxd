package com.example.uniunboxd.fragments.university;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.SearchController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.models.CourseSearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchUniversityFragment extends Fragment {

    private List<CourseSearchResult> results = new ArrayList<>();

    public SearchUniversityFragment() {
        // Required public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView search = view.findViewById(R.id.search);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String text) {
                try {
                    results.addAll(SearchController.searchCourses(text, getContext()));
                } catch (Exception e) {
                    // Nothing lol
                }
                return false;
            }

            public boolean onQueryTextChange(String text) {
                return false;
            }
        });

        return null;
    }
}
