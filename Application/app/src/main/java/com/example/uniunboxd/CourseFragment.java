package com.example.uniunboxd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_univerisity, container, false);
        TextView name = view.findViewById(R.id.courseName);
        //TODO: Make API Call and set properties of course

        LinearLayout linearLayout = view.findViewById(R.id.reviewList);
        //TODO: Base the number of calls on the API Call
        for(int i = 0; i < 10; ++i) {
            //TODO: API Call to get this entity:
            ReviewListItem reviewListItem = new ReviewListItem(i,3, "yappa", "yapper");
            linearLayout.addView(reviewListItemView(inflater, container, savedInstanceState, reviewListItem));
        }
        return view;
    }

    private View reviewListItemView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState, ReviewListItem reviewListItem) {
        View view = inflater.inflate(R.layout.review_list_item, container, false);
        TextView comment = view.findViewById(R.id.ReviewListItem_Comment);
        RatingBar ratingBar = view.findViewById(R.id.ReviewListItem_RatingBar);
        TextView posterName = view.findViewById(R.id.ReviewListItem_PosterName);
        ImageView posterIcon = view.findViewById(R.id.ReviewListItem_PosterIcon);
        //TODO: set up the id

        comment.setText(reviewListItem.Comment);
        posterName.setText(reviewListItem.PosterName);
        ratingBar.setRating(reviewListItem.Rating);
        return view;
    }
}
