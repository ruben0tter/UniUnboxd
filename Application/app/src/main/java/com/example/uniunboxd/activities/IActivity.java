package com.example.uniunboxd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.StackHandler;

import java.util.List;

public abstract class IActivity extends AppCompatActivity {

    OnBackPressedCallback backPressed = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            goBack();
        }
    };

    StackHandler fragmentHistory = StackHandler.getInstance();

    public void goBack() {
        if (fragmentHistory.empty()) {
            Log.e("GoBack", "Fragment history is empty");
            return;
        }
        replaceFragment(fragmentHistory.pop(), false);
    }

    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Intent i = new Intent(this.getApplicationContext(), activity);
        this.startActivity(i);
        fragmentHistory = null;
    }

    public void replaceFragment(Fragment fragment, boolean remember) {
        if (remember) {
            Fragment f = getVisibleFragment();
            if (f != null) fragmentHistory.push(f);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state
        Fragment f = getVisibleFragment();
        if (f != null) {
            fragmentHistory.push(f);
        }
    }
}
