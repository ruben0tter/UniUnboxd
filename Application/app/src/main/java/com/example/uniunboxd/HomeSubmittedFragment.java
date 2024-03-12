package com.example.uniunboxd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeSubmittedFragment extends HomeFragment {
    public HomeSubmittedFragment() {
        // Empty constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_uni_submitted, container, false);
    }
}
