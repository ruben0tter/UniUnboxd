package com.example.uniunboxd.activities;

import android.os.Bundle;

import com.example.uniunboxd.R;
import com.example.uniunboxd.databinding.ActivityProfessorBinding;
import com.example.uniunboxd.fragments.professor.ProfessorProfileFragment;
import com.example.uniunboxd.fragments.professor.SearchProfessorFragment;
import com.example.uniunboxd.utilities.JWTValidation;
import com.example.uniunboxd.utilities.StackHandler;

/**
 * ProfessorActivity is an activity that extends IActivity, which provides methods for handling back press, replacing activities and fragments, and saving instance state.
 * It uses ActivityProfessorBinding for view binding, which replaces findViewById calls.
 * This activity is specific to the professor user type and includes a navigation menu for profile and search.
 */
public class ProfessorActivity extends IActivity {
    // Binding instance for the activity's view
    ActivityProfessorBinding binding;

    /**
     * This method is called when the activity is starting.
     * It inflates the activity's layout, sets the content view, adds a back press callback, sets up the navigation menu, and sets up the initial fragment.
     * If there are fragments in the fragment history, it goes back to the previous fragment.
     * Otherwise, it replaces the fragment with the ProfessorProfileFragment.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this activity
        binding = ActivityProfessorBinding.inflate(getLayoutInflater());
        // Set the inflated layout as the content view
        setContentView(binding.getRoot());

        // Add a back press callback
        getOnBackPressedDispatcher().addCallback(backPressed);

        // Set up the navigation menu
        setNavigationMenu();

        // Get the StackHandler instance
        StackHandler stackHandler = StackHandler.getInstance();
        // If there are fragments in the fragment history, go back to the previous fragment
        if (stackHandler.stack != null && !stackHandler.empty()) {
            fragmentHistory = stackHandler.stack;
            goBack();
        } else {
            // Otherwise, replace the fragment with the ProfessorProfileFragment
            int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
            replaceFragment(new ProfessorProfileFragment(id), false);
        }
    }

    /**
     * This method sets up the navigation menu.
     * It sets an item selected listener on the bottom navigation view.
     * When an item is selected, it replaces the fragment with the appropriate fragment based on the item's id and clears the fragment history.
     */
    public void setNavigationMenu() {
        binding.bottomNavigationViewProfessor.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.profile) {
                // If the profile item is selected, replace the fragment with the ProfessorProfileFragment
                int id = Integer.parseInt(JWTValidation.getTokenProperty(this, "sub"));
                replaceFragment(new ProfessorProfileFragment(id), false);
            } else if (itemId == R.id.search) {
                // If the search item is selected, replace the fragment with the SearchProfessorFragment
                replaceFragment(new SearchProfessorFragment(), false);
            }
            // Clear the fragment history
            fragmentHistory.removeAllElements();
            return true;
        });
    }
}
