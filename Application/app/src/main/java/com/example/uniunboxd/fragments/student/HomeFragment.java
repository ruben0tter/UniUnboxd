package com.example.uniunboxd.fragments.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.fragments.university.CreateCourseFragment;
import com.example.uniunboxd.utilities.JWTValidation;

public class HomeFragment extends Fragment implements View.OnClickListener {
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
        Button signOut = (Button) view.findViewById(R.id.signOut);
        signOut.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            replaceFragment(new CourseFragment(1));
        } else if (id == R.id.createCourse) {
            replaceFragment(new CreateCourseFragment());
        } else if (id == R.id.signOut) {
            JWTValidation.deleteToken(getActivity());
            ((IActivity) getActivity()).replaceActivity(MainActivity.class);
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
