package com.example.uniunboxd.models.course;

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
public class AssignedProfessorModelTest {
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

    @Test
    public void mainTest() {
        assertTrue(true);

        LayoutInflater inflater = act.getLayoutInflater();
        View listView = inflater.inflate(R.layout.fragment_profile_uni, null, false);
        LinearLayout layout = listView.findViewById(R.id.applications_list);

        CreateCourseFragment f = new CreateCourseFragment();


        AssignedProfessorModel a = new AssignedProfessorModel(123, "Bart", "bart@tue.nl");
        a.CreateView(inflater, layout, f);

    }
}
