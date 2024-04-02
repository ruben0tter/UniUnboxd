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

public class SearchStudentFragment extends Fragment {

    private List<SearchResult> results = new ArrayList<>();
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

        usersButton.setOnClickListener(v -> {
            SEARCH_COURSES = false;
            coursesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            usersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

            loadMore.setVisibility(View.INVISIBLE);
            resultsLayout.removeAllViews();
            currentQuery = "";
        });

        coursesButton.setOnClickListener(v -> {
            SEARCH_COURSES = true;
            coursesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            usersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

            loadMore.setVisibility(View.INVISIBLE);
            resultsLayout.removeAllViews();
            currentQuery = "";
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

    private int searchCourses(String text) {
        try {
            List<CourseSearchResult> courses = SearchController.searchCourses(text, getContext());
            Log.d("SEARCH", "Got " + courses.size() + " courses ");
            getActivity().runOnUiThread(() -> {
                results.addAll(courses);
                for (CourseSearchResult course : courses) {
                    View view = course.createView(getLayoutInflater(), getActivity());
                    view.setOnClickListener(v -> {
                        ((IActivity) getActivity()).replaceFragment(new CourseFragment(course.Id), true);
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

    private int searchUsers(String text) {
        try {
            List<UserSearchResult> users = SearchController.searchUsers(text, getContext());
            getActivity().runOnUiThread(() -> {
                results.addAll(users);
                for (UserSearchResult user : users) {
                    View view = user.createView(getLayoutInflater(), getActivity());
                    view.setOnClickListener(v -> {
                        if(user.UserType == 0)
                            ((IActivity) getActivity()).replaceFragment(new StudentProfileFragment(user.Id), true);
                        else
                            ((IActivity) getActivity()).replaceFragment(new ProfessorProfileFragment(user.Id), true);
                    });
                    resultsLayout.addView(view);
                }
            });
            return users.size();
        } catch (Exception e) {
            Log.e("APP", "failed to search courses: " + e.toString());
        }
        return 0;
    }
}