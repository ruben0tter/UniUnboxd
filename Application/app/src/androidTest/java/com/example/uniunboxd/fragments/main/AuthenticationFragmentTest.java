package com.example.uniunboxd.fragments.main;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.utilities.JWTValidation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@RunWith(AndroidJUnit4.class)
public class AuthenticationFragmentTest {

    private Activity getActivityInstance() {
        return activityRule.launchActivity(null);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

   MainActivity act;
    @Before
    public void setUp(){
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);
        act = (MainActivity) getActivityInstance();
        act.replaceFragment(new AuthenticationFragment(), true);
    }

    @Test
    public void testEmailField() {
        String email = "test@example.com";

        // Fill in the email and password
        Espresso.onView(withId(R.id.email_input)).perform(typeText(email));

        // Check it is displayed
        Espresso.onView(ViewMatchers.withId(R.id.email_input)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.email_input)).check(matches(withText(email)));
    }

    @Test
    public void testPasswordField() {
        String password = "password";

        Espresso.onView(withId(R.id.password_input)).perform(typeText(password));

        Espresso.onView(ViewMatchers.withId(R.id.password_input)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.password_input)).check(matches(withText(password)));
    }

    @Test
    public void testSignUp() {
        Espresso.onView(withId(R.id.password_input)).perform(typeText("h"));
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_button)).perform(click());
        Espresso.onView(withId(R.id.sign_up_button)).check(matches((isEnabled())));
    }

    @Test
    public void checkSignInText() {
        Espresso.onView(withId(R.id.sign_in_text)).check(matches(withText("Sign in to UniUnboxd")));
    }

    @Test
    public void checkEmailText() {
       Espresso.onView(withId(R.id.email_text)).check(matches(withText("Email")));
    }

    @Test
    public void checkPasswordText() {
        Espresso.onView(withId(R.id.password_text)).check(matches(withText("Password")));
    }

    @Test
    public void checkAccountText() {
        Espresso.onView(withId(R.id.no_account_text)).check(matches(withText("No account?")));
    }
}