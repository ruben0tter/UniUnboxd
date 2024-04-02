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

import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

public class ReviewFragment extends Fragment implements View.OnClickListener {
    private final int id;
    private Review review;
    private boolean isReviewTabActive = false;
    private boolean isReviewLiked = false;

    private NestedScrollView reviewPage;
    private ConstraintLayout repliesPage;
    private LinearLayout replies;

    private TextView reviewTab;
    private TextView repliesTab;
    private EditText replyInput;

    private ImageView likeReview;
    private TextView likeText;
    private TextView likeCount;

    public ReviewFragment(int id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        if (review != null) {
            review.createView(view, inflater, container, this);

            int userId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
            String userType = JWTValidation.getTokenProperty(getActivity(), "typ");

            if (review.Student.Id != userId) {
                editReview.setVisibility(View.GONE);
                flagReview.setVisibility(View.VISIBLE);
            } else {
                editReview.setVisibility(View.VISIBLE);
                flagReview.setVisibility(View.GONE);
            }

            if (userType.equals("Student") && userId != review.Student.Id) {
                likeReview.setOnClickListener(this);
                if (review.StudentLikes.contains(userId)) {
                    isReviewLiked = true;
                    likeReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.like_filled));
                    likeText.setText("Liked");
                }
            } else {
                likeReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.like_filled));

                likeText.setVisibility(View.GONE);
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
        } else if (id == R.id.like) {
            changeLikeStatus();
        } else if (id == R.id.flagReview) {
            flagReview();
        }
    }

    private void goToRepliesTab(View view) {
        if (isReviewTabActive) {
            reviewPage.setVisibility(View.GONE);
            reviewTab.setTypeface(null, Typeface.NORMAL);
            repliesPage.setVisibility(View.VISIBLE);
            repliesTab.setTypeface(null, Typeface.BOLD);
            isReviewTabActive = false;
        }
    }

    private void goToReviewTab(View view) {
        if (!isReviewTabActive) {
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

        if (reply != null) {
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
        if (replies.getChildCount() != 0) {
            View lastReply = replies.getChildAt(replies.getChildCount() - 1);
            View replyDivider = lastReply.findViewById(R.id.replyDivider);
            replyDivider.setVisibility(View.VISIBLE);
        }

        // Place newly posted reply.
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        replies.addView(reply.createView(inflater, null, this, true));
    }

    private void redirectToProfile() {
        if (!review.IsAnonymous) {
            ((IActivity) getActivity()).replaceFragment(new StudentProfileFragment(review.Student.Id), true);
        }
    }

    private void redirectToCourse() {
        ((IActivity) getActivity()).replaceFragment(new CourseFragment(review.Course.Id), true);
    }

    private void redirectToEditReview() {
        ReviewModel r = createReviewModel();
        CourseModel c = createCourseModel();
        ((IActivity) getActivity()).replaceFragment(new WriteReviewFragment(c, r), true);
    }

    private ReviewModel createReviewModel() {
        return new ReviewModel(review.Id, review.Rating,
                review.Comment, review.IsAnonymous, review.Course.Id);
    }

    private CourseModel createCourseModel() {
        return new CourseModel(review.Course.Id, review.Course.Name,
                review.Course.Code, review.Course.Image);
    }

    private void changeLikeStatus() {
        if (isReviewLiked) {
            likeReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.like_open));
            likeText.setText("Like?");
            review.LikeCount--;
            likeCount.setText(String.format("%d likes", review.LikeCount));
        } else {
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
                if (isReviewLiked) {
                    ReviewController.like(review.Id, getActivity());
                } else {
                    HttpURLConnection con = ReviewController.unlike(review.Id, getActivity());

                    Log.i("LIKE", con.getResponseMessage());
                }
            } catch (Exception e) {
                Log.e("APP", "Failed to register like change: " + e.toString());
            }

        });
    }

    private void flagReview() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.FlagReview);
        builder.setTitle("Flag Review");

        View viewInflated= LayoutInflater.from(getContext()).inflate(R.layout.flag_review_message_pop_up, (ViewGroup) getView(), false);
// Set up the input
        final EditText message = viewInflated.findViewById(R.id.messageInput);
        builder.setView(viewInflated);

// Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> AsyncTask.execute(() -> {
            try {
                HttpURLConnection con = ReviewController.flagReview(new FlagReviewModel(review.Id, message.getText().toString()), getActivity());

                Log.i("Flag Review", "Code: " + con.getResponseCode());
            } catch (Exception e) {
                Log.e("APP", "Failed to register like change: " + e.toString());
            }

        }));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
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
        try {
            review = ReviewController.getReview(id, fragmentActivities[0]);
        } catch (Exception e) {
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
        try {
            reply = ReplyController.postReply(model, fragmentActivities[0]);
        } catch (Exception e) {
            Log.e("ERR", "Couldn't get reply" + e.toString());
        }
        return reply;
    }
}