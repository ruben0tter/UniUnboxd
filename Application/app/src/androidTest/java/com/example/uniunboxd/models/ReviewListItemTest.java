package com.example.uniunboxd.models;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.utilities.JWTValidation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ReviewListItemTest {
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
    static final String TOKEN_UNI = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXAiOiJVbml2ZXJzaXR5In0.ySGuYNEs_4V2rjHnYkn5e5f1IFTAEE5VcEmcquyZsZI";
    static final String TOKEN_STUDENT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXAiOiJTdHVkZW50In0.Witchok0pK4jmUsPKtGLnHeE5ZiU9iSTHmei6ps1ECo";

    @Test
    public void mainTest() {
        assertTrue(true);
        JWTValidation.placeToken(TOKEN_UNI, act);
        ReviewListItem r = new ReviewListItem(123, 4.9f, "based course", true, 123, new ReviewPoster(123, "martin", IMAGE));
        r.createView(act.getLayoutInflater(), act.findViewById(android.R.id.content));

        JWTValidation.placeToken(TOKEN_STUDENT, act);
        r = new ReviewListItem(123, 4.9f, "based course", true, 123, null);
        r.createView(act.getLayoutInflater(), act.findViewById(android.R.id.content));

        JWTValidation.deleteToken(act);
    }
}
