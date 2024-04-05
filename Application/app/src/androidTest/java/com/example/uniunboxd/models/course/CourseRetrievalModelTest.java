package com.example.uniunboxd.models.course;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.rule.ActivityTestRule;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.fragments.main.AuthenticationFragment;
import com.example.uniunboxd.models.ReviewListItem;
import com.example.uniunboxd.models.ReviewPoster;
import com.example.uniunboxd.utilities.JWTValidation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

@RunWith(JUnit4.class)
public class CourseRetrievalModelTest {


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
    static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXAiOiJTdHVkZW50In0.Witchok0pK4jmUsPKtGLnHeE5ZiU9iSTHmei6ps1ECo";

    @Test
    public void mainTest() {
        assertTrue(true);

        LayoutInflater inflater = act.getLayoutInflater();
        View listView = inflater.inflate(R.layout.fragment_profile_uni, null, false);
        LinearLayout layout = listView.findViewById(R.id.applications_list);

        AuthenticationFragment f = new AuthenticationFragment();
        FragmentManager fragmentManager = ((AppCompatActivity) act).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, f);
        fragmentTransaction.commit();

        JWTValidation.placeToken(TOKEN, act);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // nothing
        }

        CourseRetrievalModel c = new CourseRetrievalModel(123, "Algo", "2222", 4.1f, 4.3f, "based course", "Bart", IMAGE, IMAGE, 5, new ArrayList<>(), "tue", new ArrayList<>(), new ArrayList<>(), new ReviewListItem(123, 4.6f, "based", true, 123, new ReviewPoster(123, "martin", IMAGE)));
        c.createView(inflater, layout, f.getActivity());

        JWTValidation.deleteToken(act);

    }
}
