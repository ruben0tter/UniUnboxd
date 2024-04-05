package com.example.uniunboxd.fragments.student;

import android.app.AlertDialog;
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
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.ReplyController;
import com.example.uniunboxd.API.ReviewController;
import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.DTO.FlagReviewModel;
import com.example.uniunboxd.DTO.ReplyModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.university.CourseFragment;
import com.example.uniunboxd.models.review.Reply;
import com.example.uniunboxd.models.review.Review;
import com.example.uniunboxd.utilities.JWTValidation;

import java.util.concurrent.ExecutionException;

/**
 * ReviewFragment class that represents the review screen.
 */
public class ReviewFragment extends Fragment implements View.OnClickListener {

    // Variables
    private int id; // The review's ID
    private Review review; // The review object
    private boolean isReviewTabActive = false; // Flag indicating if the review tab is active
    private boolean isReviewLiked = false; // Flag indicating if the review is liked

    // UI elements
    private NestedScrollView reviewPage; // The review page layout
    private ConstraintLayout repliesPage; // The replies page layout
    private LinearLayout replies; // The container for replies

    private TextView reviewTab; // The review tab
    private TextView repliesTab; // The replies tab
    private EditText replyInput; // The input field for replying

    private ImageView likeReview; // The like button for the review
    private TextView likeText; // The text indicating the like status
    private TextView likeCount; // The count of likes


    /**
     * Necessary empty constructor.
     */
    public ReviewFragment() {
    }

    /**
     * Constructor for the ReviewFragment class.
     *
     * @param id The review's ID.
     */
    public ReviewFragment(int id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the review fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the review fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (id == 0)
            return null;

        View view = inflater.inflate(R.layout.fragment_review, container, false);

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
        ImageView flagReview = view.findViewById(R.id.flagReview);
        flagReview.setOnClickListener(this);
        likeReview = view.findViewById(R.id.like);

        // Extra
        likeText = view.findViewById(R.id.likeText);
        likeCount = view.findViewById(R.id.likeCount);

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

        // Set review information.
        if (review != null) {
            review.createView(view, inflater, container, this);

            // Set review likes.
            int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
            String userType = JWTValidation.getTokenProperty(getActivity(), "typ");

            // If the user is not the author of the review, hide the edit button and show the flag button.
            if (review.Student.Id != userId) {
                editReview.setVisibility(View.GONE);
                flagReview.setVisibility(View.VISIBLE);
            } else {
                editReview.setVisibility(View.VISIBLE);
                flagReview.setVisibility(View.GONE);
            }

            // If the user is a student and not the author of the review, show the like button.
            if (userType.equals("Student") && userId != review.Student.Id) {
                likeReview.setOnClickListener(this);
                if (review.StudentLikes.contains(userId)) {
                    // If the user has liked the review, show the filled like button.
                    isReviewLiked = true;
                    likeReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.like_filled));
                    likeText.setText("Liked");
                }
            } else {
                // If the user is not a student or the author of the review, hide the like button.
                likeReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.like_filled));

                likeText.setVisibility(View.GONE);
            }
        }

        goToReviewTab(view);

        return view;
    }

    /**
     * Handles the click events for the review fragment.
     *
     * @param v The view.
     */
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
        } else if (id == R.id.like) {
            changeLikeStatus();
        } else if (id == R.id.flagReview) {
            flagReview();
        }
    }

    /**
     * Changes the tab to the replies tab.
     *
     * @param view The view.
     */
    private void goToRepliesTab(View view) {
        if (isReviewTabActive) {
            reviewPage.setVisibility(View.GONE);
            reviewTab.setTypeface(null, Typeface.NORMAL);
            repliesPage.setVisibility(View.VISIBLE);
            repliesTab.setTypeface(null, Typeface.BOLD);
            isReviewTabActive = false;
        }
    }

    /**
     * Changes the tab to the review tab.
     * 
     * @param view The view.
     */
    private void goToReviewTab(View view) {
        if (!isReviewTabActive) {
            repliesPage.setVisibility(View.GONE);
            repliesTab.setTypeface(null, Typeface.NORMAL);
            reviewPage.setVisibility(View.VISIBLE);
            reviewTab.setTypeface(null, Typeface.BOLD);
            isReviewTabActive = true;
        }
    }

    /**
     * Posts a reply to the review.
     */
    private void postReply() {
        ReplyModel model = createReplyModel();
        ReplyPost postReply = new ReplyPost(model);
        Reply reply;

        try {
            // Post the reply.
            reply = postReply.execute(getActivity()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If the reply is not null, add the reply to the view.
        if (reply != null) {
            addReply(reply);
            replyInput.setText("");
        } else {
            // TODO: Show notification with: "Something went wrong, please try again later."
        }
    }

    /**
     * Creates a reply model.
     * 
     * @return The reply model.
     */
    private ReplyModel createReplyModel() {
        return new ReplyModel(
                replyInput.getText().toString(),
                review.Id);
    }

    /**
     * Adds a reply to the view.
     * 
     * @param reply The reply to add.
     */
    private void addReply(Reply reply) {
        // Replace bottom reply divider.
        if (replies.getChildCount() != 0) {
            View lastReply = replies.getChildAt(replies.getChildCount() - 1);
            View replyDivider = lastReply.findViewById(R.id.replyDivider);
            replyDivider.setVisibility(View.VISIBLE);
        }

        // Place newly posted reply.
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        replies.addView(reply.createView(inflater, null, this, true));
    }

    /**
     * Redirects to the student's profile.
     */
    private void redirectToProfile() {
        // If the review is not anonymous, redirect to the student's profile.
        if (!review.IsAnonymous) {
            ((IActivity) getActivity()).replaceFragment(new StudentProfileFragment(review.Student.Id), true);
        }
    }

    /**
     * Redirects to the course.
     */
    private void redirectToCourse() {
        ((IActivity) getActivity()).replaceFragment(new CourseFragment(review.Course.Id), true);
    }

    /**
     * Redirects to the edit review screen.
     */
    private void redirectToEditReview() {
        ReviewModel r = createReviewModel();
        CourseModel c = createCourseModel();
        ((IActivity) getActivity()).replaceFragment(new WriteReviewFragment(c, r), true);
    }

    /**
     * Creates a review model.
     * 
     * @return The review model.
     */
    private ReviewModel createReviewModel() {
        return new ReviewModel(review.Id, review.Rating,
                review.Comment, review.IsAnonymous, review.Course.Id);
    }

    /**
     * Creates a course model.
     * 
     * @return The course model.
     */
    private CourseModel createCourseModel() {
        return new CourseModel(review.Course.Id, review.Course.Name,
                review.Course.Code, review.Course.Image);
    }

    /**
     * Changes the like status of the review.
     */
    private void changeLikeStatus() {
        if (isReviewLiked) {
            // If the review is liked, unlike the review.
            likeReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.like_open));
            likeText.setText("Like?");
            review.LikeCount--;
            likeCount.setText(String.format("%d likes", review.LikeCount));
        } else {
            // If the review is not liked, like the review.
            likeReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.like_filled));
            likeText.setText("Liked");
            review.LikeCount++;
            likeCount.setText(String.format("%d like%s", review.LikeCount, review.LikeCount != 1 ? "s" : ""));
        }

        isReviewLiked = !isReviewLiked;
        applyLikeStatus();
    }

    private void applyLikeStatus() {
        AsyncTask.execute(() -> {
            try {
                if (isReviewLiked) ReviewController.like(review.Id, getActivity());
                else ReviewController.unlike(review.Id, getActivity());
            } catch (Exception e) {
                Log.e("APP", "Failed to register like change: " + e);
            }
        });
    }

    private void flagReview() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.FlagReview);
        builder.setTitle("Flag Review");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.flag_review_message_pop_up, (ViewGroup) getView(), false);
        // Set up the input
        final EditText message = viewInflated.findViewById(R.id.messageInput);
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> AsyncTask.execute(() -> {
            try {
                ReviewController.flagReview(new FlagReviewModel(review.Id, message.getText().toString()), getActivity());
            } catch (Exception e) {
                Log.e("APP", "Failed to register like change: " + e);
            }

        }));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}

/**
 * Async task to get the review.
 */
class ReviewInformation extends AsyncTask<FragmentActivity, Void, Review> {

    private final int id;

    public ReviewInformation(int id) {
        this.id = id;
    }

    /**
     * Gets the review.
     *
     * @param fragmentActivities The fragment activities.
     * @return The review.
     */
    @Override
    protected Review doInBackground(FragmentActivity... fragmentActivities) {
        Review review = null;
        try {
            review = ReviewController.getReview(id, fragmentActivities[0]);
        } catch (Exception e) {
            Log.e("ERR", "Couldn't get review" + e);
        }
        return review;
    }
}

/**
 * Async task to post a reply.
 */
class ReplyPost extends AsyncTask<FragmentActivity, Void, Reply> {

    private final ReplyModel model;

    public ReplyPost(ReplyModel model) {
        this.model = model;
    }

    /**
     * Posts a reply.
     *
     * @param fragmentActivities The fragment activities.
     * @return The reply.
     */
    @Override
    protected Reply doInBackground(FragmentActivity... fragmentActivities) {
        Reply reply = null;
        try {
            reply = ReplyController.postReply(model, fragmentActivities[0]);
        } catch (Exception e) {
            Log.e("ERR", "Couldn't get reply" + e);
        }
        return reply;
    }
}