package com.example.uniunboxd.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.SearchController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.models.CourseSearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchStudentFragment extends Fragment {

    private List<CourseSearchResult> results = new ArrayList<>();
    private boolean SEARCH_COURSES = true;

    private String currentQuery = "";
    private Button usersButton;
    private Button coursesButton;
    private SearchView search;
    private Button loadMore;
    private LinearLayout resultsLayout;

    public SearchStudentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        search = view.findViewById(R.id.search);
        usersButton = view.findViewById(R.id.searchUsersButton);
        coursesButton = view.findViewById(R.id.searchCourseButton);
        loadMore = view.findViewById(R.id.load_more_button);
        loadMore.setVisibility(View.INVISIBLE);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String text) {
                try {
                    search(text);
                } catch (Exception e) {
                    // Nothing lol
                }
                return false;
            }

            public boolean onQueryTextChange(String text) {
                return false;
            }
        });

        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEARCH_COURSES = false;
                coursesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                usersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                loadMore.setVisibility(View.INVISIBLE);
                resultsLayout.removeAllViews();
                currentQuery = "";
            }
        });

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEARCH_COURSES = true;
                coursesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                usersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                loadMore.setVisibility(View.INVISIBLE);
                resultsLayout.removeAllViews();
                currentQuery = "";
            }
        });

        resultsLayout = view.findViewById(R.id.results);
        resultsLayout.addView(inflater.inflate(R.layout.search_result_course, container, false));
        resultsLayout.addView(inflater.inflate(R.layout.search_result_course, container, false));
        resultsLayout.addView(inflater.inflate(R.layout.search_result_course, container, false));

        return view;
    }

    public void search(String text) {
        loadMore.setVisibility(View.VISIBLE);

        if(!currentQuery.equals(text)) {
            for(CourseSearchResult r: results) {
                results.remove(r);
            }
            resultsLayout.removeAllViews();
            currentQuery = text;
        }

        try {
            SearchController.searchCourses(text, getContext());
        } catch (Exception e) {
            resultsLayout.removeAllViews();
        }
    }
}