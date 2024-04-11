package com.example.uniunboxd.models;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import com.example.uniunboxd.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UserSearchResultTest {
    private Activity getActivityInstance() {
        return activityRule.launchActivity(null);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    Activity act;

    @Before
    public void setup() {
        act = getActivityInstance();
    }

    static final String IMAGE = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAFVJREFUWEft00EKACAIBdHv/Q9du5AgkBREmFatanqVqXlY8/4iAIExAks6D9bP059ojED6pK8FogKl7D4mGnAfoCzoN6DsSghAAAEEEEAAAQQQaBfYPeEEIcuoSBQAAAAASUVORK5CYII=";

    @Test
    public void mainTest() {
        assertTrue(true);

        UserSearchResult a = new UserSearchResult(123, "martin", IMAGE, 0);
        a.createView(act.getLayoutInflater(), act);

        UserSearchResult b = new UserSearchResult(123, "martin", null, 0);
        a.createView(act.getLayoutInflater(), act);

    }
}
