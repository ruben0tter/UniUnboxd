package com.example.uniunboxd.activities;

import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.Redirection;

import java.util.List;
import java.util.Stack;

public abstract class IActivity extends AppCompatActivity {

    OnBackPressedCallback backPressed = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            goBack();
        }
    };

    public void goBack() {
        if (fragmentHistory.empty()) {
            Log.e("GoBack", "Fragment history is empty");
            return;
        }
        Log.d("GoBack", "going back");
        replaceFragment(fragmentHistory.pop(), false);
    }

    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Redirection.replaceActivity(this, activity);
    }

    public Stack<Fragment> fragmentHistory = new Stack<>();

    public void replaceFragment(Fragment fragment, boolean remember) {
        if (remember) {
            Log.d("GoBack", "adding to history");
            Fragment f = getVisibleFragment();
            if (f == null) {
                Log.e("NIG", "failed to get current fragment");
            }
            fragmentHistory.push(f);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}
