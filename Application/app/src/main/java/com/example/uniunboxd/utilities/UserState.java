package com.example.uniunboxd;

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