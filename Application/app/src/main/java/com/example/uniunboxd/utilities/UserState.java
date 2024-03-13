package com.example.uniunboxd.utilities;

import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.fragments.student.SearchFragment;
import com.example.uniunboxd.fragments.university.HomeSubmittedFragment;
import com.example.uniunboxd.fragments.university.HomeUnverifiedFragment;
import com.example.uniunboxd.fragments.university.ProfileUniversityFragment;

public class UserState {
    private final boolean SUBMITTED;
    public UserState(String userToken) {
        SUBMITTED = userToken.equals("submitted");
    }

    public SearchFragment getSearchFragment() {
        return new SearchFragment();
    }

    public HomeFragment getHomeFragment() {
        if(SUBMITTED) {
            return new HomeSubmittedFragment();
        } else {
            return new HomeUnverifiedFragment();
        }
    }

    public ProfileFragment getProfileFragment() {
        return new ProfileUniversityFragment();
    }
}