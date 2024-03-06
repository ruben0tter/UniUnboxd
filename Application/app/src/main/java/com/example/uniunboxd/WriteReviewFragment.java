package com.example.uniunboxd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WriteReviewFragment extends Fragment implements View.OnClickListener{
    EditText comment;
    RatingBar rating;
    CheckBox check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_write_review, container, false);

        Button sign_in = (Button) view.findViewById(R.id.post_button);
        sign_in.setOnClickListener(this);
        Button sign_up = (Button) view.findViewById(R.id.delete_button);
        sign_up.setOnClickListener(this);

        comment = (EditText) view.findViewById(R.id.comment_box);
        rating = (RatingBar) view.findViewById(R.id.ratingBar);
        check = (CheckBox) view.findViewById(R.id.checkBox);

        return view;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View view) {
        int ID = view.getId();
        if (ID == R.id.post_button) {
            String userComment = comment.getText().toString(); // Comment
            float userRating = rating.getRating(); // Rating (stars, rn allows for half stars)
            boolean userAnonymous = check.isChecked(); // Checks whether anonymous box is selected
            // post the review
        } else if (ID == R.id.delete_button) {
            //delete the review
        }
    }

}
