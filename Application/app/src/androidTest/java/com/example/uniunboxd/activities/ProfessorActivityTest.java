package com.example.uniunboxd.activities;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.uniunboxd.utilities.JWTValidation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProfessorActivityTest {
    private Activity getActivityInstance() {
        return activityRule.launchActivity(null);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS);

    Activity act;

    @Before
    public void setup() {
        act = getActivityInstance();
    }

    static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjMifQ.oULSeVU4UsKJL5nxadn3y-HVxNLHeYcDk_YvSt7jb5k";

    @Test
    public void mainTest() {
        assertTrue(true);

        JWTValidation.placeToken(TOKEN, act);

        Intent i = new Intent(act, ProfessorActivity.class);
        act.startActivity(i);


    }
}
