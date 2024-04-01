package com.example.uniunboxd.fragments.university;

import static com.example.uniunboxd.API.SearchController.SEARCH_LIMIT;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.SearchController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.CourseFragment;
import com.example.uniunboxd.models.CourseSearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchUniversityFragment extends Fragment {

    private List<CourseSearchResult> results = new ArrayList<>();

    private String currentQuery = "";
    private SearchView search;
    private Button loadMore;
    private LinearLayout resultsLayout;

    public SearchUniversityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_uni, container, false);

        search = view.findViewById(R.id.search);
        loadMore = view.findViewById(R.id.load_more_button);
        loadMore.setVisibility(View.INVISIBLE);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String text) {
                Log.e("APP", "searching for " + text);
                try {
                    search(text);
                } catch (Exception e) {
                    Log.e("APP", "failed to search courses: " + e);
                }
                return false;
            }

            public boolean onQueryTextChange(String text) {
                return false;
            }
        });

        resultsLayout = view.findViewById(R.id.results);

        return view;
    }

    public void search(String text) {
        if (!currentQuery.equals(text)) {
            results.clear();
            resultsLayout.removeAllViews();
            currentQuery = text;

            try {
                AsyncTask.execute(() -> {
                    int result_count = searchCourses(text);

                    if (result_count < SEARCH_LIMIT) {
                        loadMore.setVisibility(View.INVISIBLE);
                    } else {
                        loadMore.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                Log.e("Search", e.toString());
                resultsLayout.removeAllViews();
            }
        }
    }

    private int searchCourses(String text) {
        try {
            List<CourseSearchResult> courses = SearchController.searchUniversity(text, getContext());
            getActivity().runOnUiThread(() -> {
                results.addAll(courses);
                for (CourseSearchResult course : courses) {
                    View view = course.createView(getLayoutInflater(), getActivity());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.Id), true);
                        }
                    });
                    resultsLayout.addView(view);
                }
            });
            return courses.size();
        } catch (Exception e) {
            Log.e("APP", "failed to search courses: " + e.toString());
        }
        return 0;
    }
}