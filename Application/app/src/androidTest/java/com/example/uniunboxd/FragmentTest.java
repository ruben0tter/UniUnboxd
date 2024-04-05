package com.example.uniunboxd;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.activities.MainActivity;
import com.example.uniunboxd.fragments.main.AuthenticationFragment;
import com.example.uniunboxd.fragments.main.RegistrationFragment;
import com.example.uniunboxd.fragments.professor.ProfessorEditFragment;
import com.example.uniunboxd.fragments.professor.ProfessorProfileFragment;
import com.example.uniunboxd.fragments.professor.SearchProfessorFragment;
import com.example.uniunboxd.fragments.student.HomeFragment;
import com.example.uniunboxd.fragments.student.ReviewFragment;
import com.example.uniunboxd.fragments.student.SearchStudentFragment;
import com.example.uniunboxd.fragments.student.StudentEditFragment;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.fragments.student.WriteReviewFragment;
import com.example.uniunboxd.fragments.university.ApplicationsFragment;
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.example.uniunboxd.fragments.university.CreateCourseFragment;
import com.example.uniunboxd.fragments.university.HomeSubmittedFragment;
import com.example.uniunboxd.fragments.university.HomeUnverifiedFragment;
import com.example.uniunboxd.fragments.university.SearchUniversityFragment;
import com.example.uniunboxd.fragments.university.UniversityHomeFragment;
import com.example.uniunboxd.models.course.CourseEditModel;
import com.example.uniunboxd.models.professor.ProfessorEditModel;
import com.example.uniunboxd.models.student.NotificationSettings;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.models.student.StudentProfileModel;
import com.example.uniunboxd.utilities.JWTValidation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class FragmentTest {
    private Activity getActivityInstance() {
        return activityRule.launchActivity(null);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES);

    Activity act;

    @Before
    public void setup() {
        act = getActivityInstance();
    }

    static final String IMAGE = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAFVJREFUWEft00EKACAIBdHv/Q9du5AgkBREmFatanqVqXlY8/4iAIExAks6D9bP059ojED6pK8FogKl7D4mGnAfoCzoN6DsSghAAAEEEEAAAQQQaBfYPeEEIcuoSBQAAAAASUVORK5CYII=";

    @Test
    public void testFragments() {
        JWTValidation.placeToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXAiOiJVbml2ZXJzaXR5In0.ySGuYNEs_4V2rjHnYkn5e5f1IFTAEE5VcEmcquyZsZI", act);

        replaceFragment(new AuthenticationFragment());
        replaceFragment(new RegistrationFragment());

        replaceFragment(new ProfessorEditFragment(new ProfessorEditModel(1234, "bart", IMAGE)));
        replaceFragment(new ProfessorProfileFragment(1234));
        replaceFragment(new SearchProfessorFragment());

        replaceFragment(new HomeFragment());
        replaceFragment(new ReviewFragment(1234));
        replaceFragment(new SearchStudentFragment());
        replaceFragment(new StudentEditFragment(new StudentEditModel(new StudentProfileModel(123, "martin", "tue", IMAGE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new NotificationSettings(123, true, true, true, true, true, true), 2))));
        replaceFragment(new StudentProfileFragment(1234));
        replaceFragment(new WriteReviewFragment(new CourseModel(1234, "Algo", "22222", IMAGE)));

        replaceFragment(new ApplicationsFragment());
        replaceFragment(new CourseFragment(1234));

        replaceFragment(new CreateCourseFragment(new CourseEditModel(1234, "Algo", "22222", "based course", "bart", IMAGE, IMAGE, new ArrayList<>())));
        replaceFragment(new HomeSubmittedFragment());
        replaceFragment(new HomeUnverifiedFragment());
        replaceFragment(new SearchUniversityFragment());
        replaceFragment(new UniversityHomeFragment());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) act).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
