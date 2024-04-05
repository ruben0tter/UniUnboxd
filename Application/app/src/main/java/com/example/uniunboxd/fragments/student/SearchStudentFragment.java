package com.example.uniunboxd.fragments.student;

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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.SearchController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.professor.ProfessorProfileFragment;
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.example.uniunboxd.models.CourseSearchResult;
import com.example.uniunboxd.models.SearchResult;
import com.example.uniunboxd.models.UserSearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchStudentFragment class that represents the search screen for students.
 */
public class SearchStudentFragment extends Fragment {


    // List to store search results
    private final List<SearchResult> results = new ArrayList<>();

    // Flag to indicate whether to search for courses or users
    private boolean SEARCH_COURSES = true;

    // Current search query
    private String currentQuery = "";

    // Buttons for switching between searching for users or courses
    private Button usersButton;
    private Button coursesButton;

    // Search view for entering search queries
    private SearchView search;

    // Button for loading more search results
    private Button loadMore;

    // Layout for displaying search results
    private LinearLayout resultsLayout;

    public SearchStudentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the search student fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the search student fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for the search student fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the search view, buttons, and load more button
        search = view.findViewById(R.id.search);
        usersButton = view.findViewById(R.id.searchUsersButton);
        coursesButton = view.findViewById(R.id.searchCourseButton);
        loadMore = view.findViewById(R.id.load_more_button);

        // Set the load more button to be invisible initially
        loadMore.setVisibility(View.INVISIBLE);

        // Set the search view listener.
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

        // Set the load more button listener.
        usersButton.setOnClickListener(v -> {
            // Set the search flag to search for users
            SEARCH_COURSES = false;
            coursesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            usersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

            // Hide the load more button and clear the results layout
            loadMore.setVisibility(View.INVISIBLE);
            resultsLayout.removeAllViews();
            currentQuery = "";
        });

        // Set the load more button listener.
        coursesButton.setOnClickListener(v -> {
            // Set the search flag to search for courses
            SEARCH_COURSES = true;
            coursesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            usersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

            // Hide the load more button and clear the results layout
            loadMore.setVisibility(View.INVISIBLE);
            resultsLayout.removeAllViews();
            currentQuery = "";
        });

        // Set the load more button listener.
        resultsLayout = view.findViewById(R.id.results);
        return view;
    }

    /**
     * Searches for the given text.
     *
     * @param text The text to search for.
     */
    public void search(String text) {
        if (!currentQuery.equals(text)) {
            results.clear();
            resultsLayout.removeAllViews();
            currentQuery = text;

            try {
                // Search for the text in the background.
                AsyncTask.execute(() -> {
                    int result_count;
                    if (SEARCH_COURSES) {
                        result_count = searchCourses(text);
                    } else {
                        result_count = searchUsers(text);
                    }
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

    /**
     * Searches for courses with the given text.
     * @param text The text to search for.
     * @return The number of courses found.
     * 
     */
    private int searchCourses(String text) {
        try {
            // Search for courses with the given text.
            List<CourseSearchResult> courses = SearchController.searchCourses(text, getContext());
            Log.d("SEARCH", "Got " + courses.size() + " courses ");
            getActivity().runOnUiThread(() -> {
                // Add the courses to the results layout.
                results.addAll(courses);
                for (CourseSearchResult course : courses) {
                    // Create a view for the course.
                    View view = course.createView(getLayoutInflater(), getActivity());
                    view.setOnClickListener(v -> {
                        // Replace the fragment with the course fragment.
                        ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.Id), true);
                    });
                    // Add the view to the results layout.
                    resultsLayout.addView(view);
                }
            });
            return courses.size();
        } catch (Exception e) {
            Log.e("APP", "failed to search courses: " + e);
        }
        return 0;
    }


    /**
     * Searches for users with the given text.
     * @param text The text to search for.
     * @return The number of users found.
     */
    private int searchUsers(String text) {
        try {
            List<UserSearchResult> users = SearchController.searchUsers(text, getContext());
            getActivity().runOnUiThread(() -> {
                results.addAll(users);
                // Add the users to the results layout.
                for (UserSearchResult user : users) {
                    // Create a view for the user.
                    View view = user.createView(getLayoutInflater(), getActivity());

                    view.setOnClickListener(v -> {
                        if (user.UserType == 0)
                            // Replace the fragment with the student profile fragment.
                            ((IActivity) getActivity()).replaceFragment(new StudentProfileFragment(user.Id), true);
                        else
                            // Replace the fragment with the professor profile fragment.
                            ((IActivity) getActivity()).replaceFragment(new ProfessorProfileFragment(user.Id), true);
                    });
                    // Add the view to the results layout.
                    resultsLayout.addView(view);
                }
            });
            // Return the number of users found.
            return users.size();
        } catch (Exception e) {
            Log.e("APP", "failed to search courses: " + e);
        }
        return 0;
    }
}