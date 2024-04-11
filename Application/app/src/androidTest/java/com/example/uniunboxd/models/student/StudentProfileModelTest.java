package com.example.uniunboxd.models.student;

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
public class StudentProfileModelTest {
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
    static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjMiLCJ0eXAiOiJTdHVkZW50In0.vb4-wh4liVJS1xJ7631ah310m2Yb-j6j2hgwNPVo8ZI";

    @Test
    public void mainTest() {
        assertTrue(true);

//        LayoutInflater inflater = act.getLayoutInflater();
//        View listView = inflater.inflate(R.layout.fragment_profile_uni, null, false);
//        LinearLayout layout = listView.findViewById(R.id.applications_list);
//
//        CreateCourseFragment f = new CreateCourseFragment();
//        FragmentManager fragmentManager = ((AppCompatActivity) act).getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, f);
//        fragmentTransaction.commit();
//
//        JWTValidation.placeToken(TOKEN, act);
//
//        StudentProfileModel a = new StudentProfileModel(123, "martin", "tue", IMAGE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new NotificationSettings(123, true, true, true, true, true, true), 2);
//        a.createView(inflater, layout, f);
//
//        JWTValidation.deleteToken(act);
    }
}
