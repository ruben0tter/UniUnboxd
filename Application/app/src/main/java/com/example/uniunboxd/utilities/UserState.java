package com.example.uniunboxd.utilities;

import com.example.uniunboxd.fragments.student.ProfileFragment;
import com.example.uniunboxd.fragments.student.SearchStudentFragment;
import com.example.uniunboxd.fragments.university.ApplicationsFragment;

public class UserState {
    private final boolean SUBMITTED;
    public UserState(String userToken) {
        SUBMITTED = userToken.equals("submitted");
    }

    public SearchStudentFragment getSearchFragment() {
        return new SearchStudentFragment();
    }

    /*
    public HomeFragment getHomeFragment() {
        if(SUBMITTED) {
            return new HomeSubmittedFragment();
        } else {
            return new HomeUnverifiedFragment();
        }
    }
    */

    public ProfileFragment getProfileFragment() {
        return new ApplicationsFragment();
    }
}