package com.example.uniunboxd.fragments.main;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.uniunboxd.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class RegistrationFragmentTest {

    @Test
    public void testEmailField() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);

        String email = "test@example.com";

        // Fill in the email and password
        Espresso.onView(withId(R.id.email)).perform(typeText(email));

        // Check it is displayed
        Espresso.onView(ViewMatchers.withId(R.id.email)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.email)).check(matches(withText(email)));
    }

    @Test
    public void testPasswordField() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);

        String password = "password";

        Espresso.onView(withId(R.id.password)).perform(typeText(password));

        Espresso.onView(ViewMatchers.withId(R.id.password)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.password)).check(matches(withText(password)));
    }

    @Test
    public void testRepeatPasswordField() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);

        String password = "password";

        Espresso.onView(withId(R.id.repeatPassword)).perform(typeText(password));

        Espresso.onView(ViewMatchers.withId(R.id.repeatPassword)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.repeatPassword)).check(matches(withText(password)));
    }

    @Test
    public void testUserType() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);

        String student = "Student";
        String university = "University";
        String professor = "Professor";

        Espresso.onView(withId(R.id.userType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is(student)))
                .perform(click());
        Espresso.onView(withId(R.id.userType))
                .check(matches(withSpinnerText(containsString(student))));

        Espresso.onView(withId(R.id.userType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is(university)))
                .perform(click());
        Espresso.onView(withId(R.id.userType))
                .check(matches(withSpinnerText(containsString(university))));

        Espresso.onView(withId(R.id.userType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is(professor)))
                .perform(click());
        Espresso.onView(withId(R.id.userType))
                .check(matches(withSpinnerText(containsString(professor))));
    }

    @Test
    public void testSignInButton() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);
        Espresso.onView(withId(R.id.email)).perform(typeText("g"));
        Espresso.onView(withId(R.id.signIn)).perform(click());
        Espresso.onView(withId(R.id.signIn)).check(matches(isEnabled()));
    }

    @Test
    public void testAccountText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);
        Espresso.onView(withId(R.id.createAccount)).check(matches(withText("Create a new account")));
    }

    @Test
    public void testEmailText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);
        Espresso.onView(withId(R.id.emailText)).check(matches(withText("Email")));
    }

    @Test
    public void testPasswordText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);
        Espresso.onView(withId(R.id.passwordText)).check(matches(withText("Password")));
    }

    @Test
    public void testRepeatPasswordText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);
        Espresso.onView(withId(R.id.repeatPasswordText)).check(matches(withText("Repeat Password")));
    }

    @Test
    public void testUserTypeText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);
        Espresso.onView(withId(R.id.userTypeText)).check(matches(withText("Type of User")));
    }

    @Test
    public void testAlreadyHaveAccountText() {
        // Launch the fragment in the testing environment
        FragmentScenario.launchInContainer(RegistrationFragment.class, null);
        Espresso.onView(withId(R.id.signInText)).check(matches(withText("Already have an account?")));
    }
}