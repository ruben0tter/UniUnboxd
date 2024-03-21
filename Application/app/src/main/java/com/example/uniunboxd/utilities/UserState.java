package com.example.uniunboxd.utilities;

import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.fragments.student.SearchFragment;
import com.example.uniunboxd.fragments.university.ApplicationsFragment;

public class UserState {
    private final boolean SUBMITTED;
    public UserState(String userToken) {
        SUBMITTED = userToken.equals("submitted");
    }

    public SearchFragment getSearchFragment() {
        return new SearchFragment();
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

//    public StudentProfileFragment getProfileFragment() {
//        return new ApplicationsFragment();
//    }
}