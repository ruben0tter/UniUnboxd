package com.example.uniunboxd.models.student;

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

@RunWith(JUnit4.class)
public class StudentReviewListItemTest {
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

        StudentReviewListItem a = new StudentReviewListItem(123, 4.6f, "based", new StudentReviewCourse(123, "algo", "22222", IMAGE));
        a.createView(inflater, layout, new CreateCourseFragment());

        StudentReviewListItem b = new StudentReviewListItem(123, 4.6f, "based", new StudentReviewCourse(123, "algo", "22222", null));
        b.createView(inflater, layout, new CreateCourseFragment());

    }
}
