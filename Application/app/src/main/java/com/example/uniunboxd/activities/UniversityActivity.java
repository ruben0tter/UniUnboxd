package com.example.uniunboxd.activities;

import android.os.Bundle;
import android.view.View;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityUniversityBinding;
import com.example.uniunboxd.fragments.university.ApplicationsFragment;
import com.example.uniunboxd.fragments.university.HomeSubmittedFragment;
import com.example.uniunboxd.fragments.university.HomeUnverifiedFragment;
import com.example.uniunboxd.fragments.university.SearchUniversityFragment;
import com.example.uniunboxd.fragments.university.UniversityHomeFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.StackHandler;

import java.util.Objects;

/**
 * UniversityActivity is an activity that extends IActivity, which provides methods for handling back press, replacing activities and fragments, and saving instance state.
 * It uses ActivityUniversityBinding for view binding, which replaces findViewById calls.
 * This activity is specific to the university user type and includes a navigation menu for home, search, and applications.
 * The navigation menu is only visible if the university's status is verified.
 */
public class UniversityActivity extends IActivity {
    // The university's status
    String status;

    // Binding instance for the activity's view
    ActivityUniversityBinding binding;

    /**
     * This method is called when the activity is starting.
     * It inflates the activity's layout, sets the content view, adds a back press callback, and initialises the activity based on the university's status.
     * If there are fragments in the fragment history, it goes back to the previous fragment.
     * Otherwise, it gets the university's status from the JWT token and initialises the activity.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this activity
        binding = ActivityUniversityBinding.inflate(getLayoutInflater());
        // Set the inflated layout as the content view
        setContentView(binding.getRoot());

        // Add a back press callback
        getOnBackPressedDispatcher().addCallback(backPressed);

        // Get the StackHandler instance
        StackHandler stackHandler = StackHandler.getInstance();
        // If there are fragments in the fragment history, go back to the previous fragment
        if (stackHandler.stack != null && !stackHandler.empty()) {
            fragmentHistory = stackHandler.stack;
            goBack();
        } else {
            // Otherwise, get the university's status from the JWT token and initialise the activity
            status = JWTValidation.getTokenProperty(this, "verified");
            initialise();
        }
    }

    /**
     * This method initialises the activity based on the university's status.
     * If the university's status is verified, it sets an item selected listener on the bottom navigation view and replaces the fragment with the UniversityHomeFragment.
     * When an item is selected, it replaces the fragment with the appropriate fragment based on the item's id and clears the fragment history.
     * If the university's status is not verified, it hides the bottom navigation view and replaces the fragment with the HomeSubmittedFragment if the status is submitted, or the HomeUnverifiedFragment otherwise.
     */
    public void initialise() {
        if (Objects.equals(status, "Verified")) {
            // If the university's status is verified
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // If the home item is selected, replace the fragment with the UniversityHomeFragment
                    replaceFragment(new UniversityHomeFragment(), false);
                } else if (itemId == R.id.search) {
                    // If the search item is selected, replace the fragment with the SearchUniversityFragment
                    replaceFragment(new SearchUniversityFragment(), false);
                } else if (itemId == R.id.applications) {
                    // If the applications item is selected, replace the fragment with the ApplicationsFragment
                    replaceFragment(new ApplicationsFragment(), false);
                }
                // Clear the fragment history
                fragmentHistory.removeAllElements();
                return true;
            });

            // Replace the fragment with the UniversityHomeFragment
            replaceFragment(new UniversityHomeFragment(), false);
        } else {
            // If the university's status is not verified
            // Hide the bottom navigation view
            binding.bottomNavigationView.setVisibility(View.GONE);
            // Replace the fragment with the HomeSubmittedFragment if the status is submitted, or the HomeUnverifiedFragment otherwise
            replaceFragment(Objects.equals(status, "Submitted") ?
                    new HomeSubmittedFragment() : new HomeUnverifiedFragment(), false);
        }
    }
}
