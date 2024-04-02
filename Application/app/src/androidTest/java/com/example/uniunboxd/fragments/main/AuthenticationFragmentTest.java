package com.example.uniunboxd.fragments.main;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.uniunboxd.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AuthenticationFragmentTest {

    @Test
    public void testEmailField() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);

        String email = "test@example.com";

        // Fill in the email and password
        Espresso.onView(withId(R.id.email_input)).perform(typeText(email));

        // Check it is displayed
        Espresso.onView(ViewMatchers.withId(R.id.email_input)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.email_input)).check(matches(withText(email)));
    }

    @Test
    public void testPasswordField() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);

        String password = "password";

        Espresso.onView(withId(R.id.password_input)).perform(typeText(password));

        Espresso.onView(ViewMatchers.withId(R.id.password_input)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.password_input)).check(matches(withText(password)));
    }

    @Test
    public void testSignUp() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);
        Espresso.onView(withId(R.id.password_input)).perform(typeText("h"));
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_button)).perform(click());
        Espresso.onView(withId(R.id.sign_up_button)).check(matches((isEnabled())));
    }

    @Test
    public void checkSignInText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);
        Espresso.onView(withId(R.id.sign_in_text)).check(matches(withText("Sign in to UniUnboxd")));
    }

    @Test
    public void checkEmailText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);
        Espresso.onView(withId(R.id.email_text)).check(matches(withText("Email")));
    }

    @Test
    public void checkPasswordText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);
        Espresso.onView(withId(R.id.password_text)).check(matches(withText("Password")));
    }

    @Test
    public void checkAccountText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(AuthenticationFragment.class, null);
        Espresso.onView(withId(R.id.no_account_text)).check(matches(withText("No account?")));
    }
}