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
import java.util.Stack;

/**
 * This is an abstract class that extends AppCompatActivity.
 * It provides methods for handling back press, replacing activities and fragments, and saving instance state.
 */
public abstract class IActivity extends AppCompatActivity {

    // Callback for handling back press
    OnBackPressedCallback backPressed = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            goBack();
        }
    };

    // Stack instance for managing fragment history
    public Stack<Fragment> fragmentHistory = new Stack<>();

    /**
     * This method is used to go back to the previous fragment.
     * If the fragment history is empty, it logs an error message.
     */
    public void goBack() {
        if (fragmentHistory.empty()) {
            Log.e("GoBack", "Fragment history is empty");
            return;
        }
        replaceFragment(fragmentHistory.pop(), false);
    }

    /**
     * This method is used to replace the current activity with a new one.
     * @param activity The class of the activity to start.
     */
    public void replaceActivity(Class<? extends AppCompatActivity> activity) {
        Intent i = new Intent(this.getApplicationContext(), activity);
        this.startActivity(i);
        StackHandler stackHandler = StackHandler.getInstance();
        stackHandler.stack = null;
    }

    /**
     * This method is used to replace the current fragment with a new one.
     * If remember is true, it adds the current fragment to the fragment history.
     * @param fragment The fragment to display.
     * @param remember Whether to remember the current fragment.
     */
    public void replaceFragment(Fragment fragment, boolean remember) {
        if (remember) {
            Fragment f = getVisibleFragment();
            if (f != null)
                fragmentHistory.push(f);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * This method is used to get the currently visible fragment.
     * @return The currently visible fragment, or null if no fragment is visible.
     */
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

    /**
     * This method is called to ask the activity to save its current dynamic state.
     * It saves the currently visible fragment to the fragment history.
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state
        Fragment f = getVisibleFragment();
        if (f != null) {
            fragmentHistory.push(f);
        }

        StackHandler stackHandler = StackHandler.getInstance();
        stackHandler.setStack(fragmentHistory);
    }
}
