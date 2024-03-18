package com.example.uniunboxd.fragments.student;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.ReplyController;
import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.DTO.ReplyModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.review.Reply;
import com.example.uniunboxd.models.review.Review;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.concurrent.ExecutionException;

public class ReviewFragment extends Fragment implements View.OnClickListener {
    private final int id;
    private Review review;
    private boolean isReviewTabActive = false;

    private ConstraintLayout reviewPage;
    private ConstraintLayout repliesPage;
    private LinearLayout replies;

    private TextView reviewTab;
    private TextView repliesTab;
    private EditText replyInput;


    public ReviewFragment(int id)
    {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_review, container, false);

        // Layouts
        reviewPage = view.findViewById(R.id.reviewPage);
        repliesPage = view.findViewById(R.id.repliesPage);
        replies = view.findViewById(R.id.replies);

        // Tabs
        reviewTab = view.findViewById(R.id.reviewTab);
        reviewTab.setOnClickListener(this);
        repliesTab = view.findViewById(R.id.repliesTab);
        repliesTab.setOnClickListener(this);

        // Input
        replyInput = view.findViewById(R.id.replyInput);

        // Buttons
        Button replyPost = view.findViewById(R.id.replyPost);
        replyPost.setOnClickListener(this);
        ImageView editReview = view.findViewById(R.id.editReview);
        editReview.setOnClickListener(this);

        // Profile Redirects
        ImageView profileImage = view.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(this);
        TextView profileName = view.findViewById(R.id.profileName);
        profileName.setOnClickListener(this);

        // Course Redirects
        TextView courseName = view.findViewById(R.id.courseName);
        courseName.setOnClickListener(this);
        ImageView courseImage = view.findViewById(R.id.courseImage);
        courseImage.setOnClickListener(this);

        // Info
        ReviewInformation getReview = new ReviewInformation(id);

        try {
            review = getReview.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(review != null) {
            review.createView(view, inflater, container, this);

            int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
            if(review.Student.Id != userId) {
                editReview.setVisibility(View.GONE);
            }
        }

        goToReviewTab(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.reviewTab) {
            goToReviewTab(v);
        } else if (id == R.id.repliesTab) {
            goToRepliesTab(v);
        } else if (id == R.id.replyPost) {
            postReply();
        } else if (id == R.id.profileImage || id == R.id.profileName) {
            redirectToProfile();
        } else if (id == R.id.courseName || id == R.id.courseImage) {
            redirectToCourse();
        } else if (id == R.id.editReview) {
            redirectToEditReview();
        }
    }

    private void goToRepliesTab(View view) {
        if(isReviewTabActive) {
            reviewPage.setVisibility(View.GONE);
            reviewTab.setTypeface(null, Typeface.NORMAL);
            repliesPage.setVisibility(View.VISIBLE);
            repliesTab.setTypeface(null, Typeface.BOLD);
            isReviewTabActive = false;
        }
    }

    private void goToReviewTab(View view) {
        if(!isReviewTabActive) {
            repliesPage.setVisibility(View.GONE);
            repliesTab.setTypeface(null, Typeface.NORMAL);
            reviewPage.setVisibility(View.VISIBLE);
            reviewTab.setTypeface(null, Typeface.BOLD);
            isReviewTabActive = true;
        }
    }

    private void postReply() {
        ReplyModel model = createReplyModel();
        ReplyPost postReply = new ReplyPost(model);
        Reply reply;

        try {
            reply = postReply.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(reply != null) {
            addReply(reply);
            replyInput.setText("");
        } else {
            // TODO: Show notification with: "Something went wrong, please try again later."
        }
    }

    private ReplyModel createReplyModel() {
        return new ReplyModel(
                replyInput.getText().toString(),
                review.Id);
    }

    private void addReply(Reply reply) {
        // Replace bottom reply divider.
        View lastReply = replies.getChildAt(replies.getChildCount() - 1);
        View replyDivider = lastReply.findViewById(R.id.replyDivider);
        replyDivider.setVisibility(View.VISIBLE);

        // Place newly posted reply.
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        replies.addView(reply.createView(inflater, null, this, true));
    }

    private void redirectToProfile() {
        if (!review.IsAnonymous) {
            ((IActivity) getActivity()).replaceFragment(new ProfileFragment(review.Student.Id));
        }
    }

    private void redirectToCourse() {
        ((IActivity) getActivity()).replaceFragment(new CourseFragment(review.Course.Id));
    }

    private void redirectToEditReview() {
        ReviewModel r = createReviewModel();
        CourseModel c = createCourseModel();
        ((IActivity) getActivity()).replaceFragment(new WriteReviewFragment(c, r));
    }

    private ReviewModel createReviewModel() {
        return new ReviewModel(review.Id, review.Rating,
                review.Comment, review.IsAnonymous, review.Course.Id);
    }

    private CourseModel createCourseModel() {
        return new CourseModel(review.Course.Id, review.Course.Name,
                review.Course.Code, review.Course.Image);
    }
}

class ReviewInformation extends AsyncTask<FragmentActivity, Void, Review> {

    private final int id;

    public ReviewInformation(int id) {
        this.id = id;
    }

    @Override
    protected Review doInBackground(FragmentActivity... fragmentActivities) {
        Review review = null;
        try{
            review = ReviewController.getReview(id, fragmentActivities[0]);
        } catch(Exception e) {
            Log.e("ERR", "Couldn't get review" + e.toString());
        }
        return review;
    }
}

class ReplyPost extends AsyncTask<FragmentActivity, Void, Reply> {

    private final ReplyModel model;

    public ReplyPost(ReplyModel model) {
        this.model = model;
    }

    @Override
    protected Reply doInBackground(FragmentActivity... fragmentActivities) {
        Reply reply = null;
        try{
            reply = ReplyController.postReply(model, fragmentActivities[0]);
        } catch(Exception e) {
            Log.e("ERR", "Couldn't get reply" + e.toString());
        }
        return reply;
    }
}