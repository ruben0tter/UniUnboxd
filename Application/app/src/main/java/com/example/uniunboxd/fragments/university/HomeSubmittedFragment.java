package com.example.uniunboxd.fragments.university;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.utilities.JWTValidation;

public class HomeSubmittedFragment extends HomeFragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_uni_submitted, container, false);

        Button signOut = (Button) view.findViewById(R.id.signOut);
        signOut.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        JWTValidation.deleteToken(getActivity());
        ((IActivity) getActivity()).replaceActivity(MainActivity.class);
    }
}
