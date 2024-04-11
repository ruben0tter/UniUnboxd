package com.example.uniunboxd.models.review;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.test.rule.ActivityTestRule;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.fragments.university.CreateCourseFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class ReviewTest {
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

        LayoutInflater inflater = act.getLayoutInflater();
        View listView = inflater.inflate(R.layout.fragment_profile_uni, null, false);
        LinearLayout layout = listView.findViewById(R.id.applications_list);
        CreateCourseFragment f = new CreateCourseFragment();

        View view = inflater.inflate(R.layout.fragment_review, layout, false);

        Review a = new Review(123, new Date(), 4.1, "based", true, new CourseHeader(123, "algo", "22222", IMAGE, IMAGE), new UserHeader(123, "martin", IMAGE), new ArrayList<>(), 10, new ArrayList<>());
        a.createView(view, inflater, layout, f);

        Reply r = new Reply("based", new UserHeader(123, "martin", IMAGE));

        a = new Review(123, new Date(), 4.1, "based", false, new CourseHeader(123, "algo", "22222", null, null), new UserHeader(123, "martin", IMAGE), List.of(r, r), 10, new ArrayList<>());
        a.createView(view, inflater, layout, f);
    }
}
