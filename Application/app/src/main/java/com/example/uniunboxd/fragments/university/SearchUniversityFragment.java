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
import com.example.uniunboxd.models.CourseSearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchUniversityFragment class that represents the search university screen.
 */
public class SearchUniversityFragment extends Fragment {


    // List to store the search results
    private final List<CourseSearchResult> results = new ArrayList<>();

    // Variable to store the current search query
    private String currentQuery = "";

    // SearchView for user input
    private SearchView search;

    // Button to load more search results
    private Button loadMore;

    // LinearLayout to display search results
    private LinearLayout resultsLayout;


    public SearchUniversityFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the view for the search university fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the search university fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_uni, container, false);

        // Set the layout variables.
        search = view.findViewById(R.id.search);
        loadMore = view.findViewById(R.id.load_more_button);
        loadMore.setVisibility(View.INVISIBLE);

        // Set the listener for the search view.
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String text) {
                Log.e("APP", "searching for " + text);
                try {
                    // Search for courses.
                    search(text);
                } catch (Exception e) {
                    Log.e("APP", "failed to search courses: " + e);
                }
                return false;
            }
            // Do nothing when the text changes.
            public boolean onQueryTextChange(String text) {
                return false;
            }
        });

        // Initialize the layout for displaying search results.
        resultsLayout = view.findViewById(R.id.results);

        return view;
    }

    /**
     * Search for courses.
     * @param text The search query.
     */
    public void search(String text) {
        // If the query is different from the current query, clear the results and search for courses.
        if (!currentQuery.equals(text)) {
            results.clear();
            resultsLayout.removeAllViews();
            currentQuery = text;

            try {
                AsyncTask.execute(() -> {
                    // Search for courses.
                    int result_count = searchCourses(text);

                    // If there are less than SEARCH_LIMIT results, hide the load more button.
                    if (result_count < SEARCH_LIMIT) {
                        loadMore.setVisibility(View.INVISIBLE);
                    } else {
                        loadMore.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                // Log the error and remove all views.
                Log.e("Search", e.toString());
                resultsLayout.removeAllViews();
            }
        }
    }

    /**
     * Search for courses.
     * @param text The search query.
     * @return The number of search results.
     */
    private int searchCourses(String text) {
        try {
            // Search for courses.
            List<CourseSearchResult> courses = SearchController.searchUniversity(text, getContext());
            getActivity().runOnUiThread(() -> {
                // Add the courses to the results.
                results.addAll(courses);
                // Add the courses to the view.
                for (CourseSearchResult course : courses) {
                    // Create the course view.
                    View view = course.createView(getLayoutInflater(), getActivity());
                    view.setOnClickListener(v -> {
                        // Replace the fragment with the course fragment.
                        ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.Id), true);
                    });
                    resultsLayout.addView(view);
                }
            });
            // Return the number of courses found.
            return courses.size();
        } catch (Exception e) {
            Log.e("APP", "failed to search courses: " + e);
        }
        return 0;
    }
}