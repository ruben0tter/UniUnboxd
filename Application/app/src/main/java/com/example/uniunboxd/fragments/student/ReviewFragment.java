package com.example.uniunboxd.fragments.student;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;

public class ReviewFragment extends Fragment implements View.OnClickListener {
    private boolean isReviewTabActive = true;

    private ConstraintLayout reviewPage;
    private ConstraintLayout repliesPage;

    private TextView reviewTab;
    private TextView repliesTab;

    public ReviewFragment(int id)
    {
        //TODO: Get course
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_review, container, false);

        // Layouts
        reviewPage = view.findViewById(R.id.reviewPage);
        repliesPage = view.findViewById(R.id.repliesPage);

        // Tabs
        reviewTab = view.findViewById(R.id.reviewTab);
        reviewTab.setOnClickListener(this);
        repliesTab = view.findViewById(R.id.repliesTab);
        repliesTab.setOnClickListener(this);

        return view;
    }

    public void goToRepliesTab(View view) {
        if(isReviewTabActive) {
            reviewPage.setVisibility(View.GONE);
            reviewTab.setTypeface(null, Typeface.NORMAL);
            repliesPage.setVisibility(View.VISIBLE);
            repliesTab.setTypeface(null, Typeface.BOLD);
            isReviewTabActive = false;
        }
    }

    public void goToReviewTab(View view) {
        if(!isReviewTabActive) {
            repliesPage.setVisibility(View.GONE);
            repliesTab.setTypeface(null, Typeface.NORMAL);
            reviewPage.setVisibility(View.VISIBLE);
            reviewTab.setTypeface(null, Typeface.BOLD);
            isReviewTabActive = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reviewTab) {
            goToReviewTab(v);
        } else if (v.getId() == R.id.repliesTab) {
            goToRepliesTab(v);
        }
    }
}
