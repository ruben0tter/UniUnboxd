package com.example.uniunboxd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment implements View.OnClickListener{
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(this);
        ImageButton courseCreate = (ImageButton) view.findViewById(R.id.createCourse);
        courseCreate.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            replaceFragment(new CourseFragment());
        }
        else if (id == R.id.createCourse) {
            replaceFragment(new CreateCourseFragment());
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
