package com.example.uniunboxd.models.course;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.DTO.ReviewModel;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.WriteReviewFragment;
import com.example.uniunboxd.models.ReviewListItem;
import com.example.uniunboxd.utilities.ImageHandler;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CourseRetrievalModel {
    public final int Id;
    public final String Name;
    public final String Code;
    public final float AnonymousRating;
    public final float NonanonymousRating;
    public final String Description;
    public final String Professor;
    public final String Image;
    public final String Banner;
    public final int UniversityId;
    public final String UniversityName;
    public final List<ReviewListItem> Reviews;
    public final List<Integer> AssignedProfessors;
    public final List<FriendReviewModel> FriendReviews;
    public final ReviewListItem YourReview;

    /**
     * Constructor for the CourseRetrievalModel class.
     *
     * @param id                The course's ID.
     * @param name              The course's name.
     * @param code              The course's code.
     * @param anonymousRating   The course's anonymous rating.
     * @param nonanonymousRating The course's non-anonymous rating.
     * @param description       The course's description.
     * @param professor         The course's professor.
     * @param image             The course's image.
     * @param banner            The course's banner.
     * @param universityId       The course's university ID.
     * @param reviews           The course's reviews.
     * @param universityName     The course's university name.
     * @param assignedProfessors The course's assigned professors.
     * @param friendReviews      The course's friend reviews.
     * @param yourReview         The course's your review.
     */
    @JsonCreator
    public CourseRetrievalModel(@JsonProperty("id") int id, @JsonProperty("name") String name,
                                @JsonProperty("code") String code, @JsonProperty("anonymousRating") float anonymousRating,
                                @JsonProperty("nonanonymousRating") float nonanonymousRating, @JsonProperty("description") String description,
                                @JsonProperty("professor") String professor, @JsonProperty("image") String image,
                                @JsonProperty("banner") String banner, @JsonProperty("universityId") int universityId,
                                @JsonProperty("reviews") List<ReviewListItem> reviews, @JsonProperty("universityName") String universityName,
                                @JsonProperty("assignedProfessors") List<Integer> assignedProfessors,
                                @JsonProperty("friendReviews") List<FriendReviewModel> friendReviews,
                                @JsonProperty("yourReview") ReviewListItem yourReview) {
        Id = id;
        Name = name;
        Code = code;
        AnonymousRating = anonymousRating;
        NonanonymousRating = nonanonymousRating;
        Description = description;
        Professor = professor;
        Image = image;
        Banner = banner;
        UniversityId = universityId;
        Reviews = reviews;
        UniversityName = universityName;
        AssignedProfessors = assignedProfessors;
        FriendReviews = friendReviews;
        YourReview = yourReview;
    }

    /**
     * Creates a view for the course retrieval model.
     *
     * @param inflater   The layout inflater.
     * @param container  The parent layout.
     * @param activity   The activity.
     * @return The view for the course retrieval model.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, FragmentActivity activity) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        TextView name = view.findViewById(R.id.courseName);
        TextView code = view.findViewById(R.id.courseCode);
        TextView professor = view.findViewById(R.id.professor);
        TextView description = view.findViewById(R.id.description);
        TextView universityName = view.findViewById(R.id.universityName);
        ImageView image = view.findViewById(R.id.courseImage);
        ImageView banner = view.findViewById(R.id.courseBanner);
        RatingBar anonRatingBar = view.findViewById(R.id.ratingBarAnonymous);
        RatingBar nonanonRatingBar = view.findViewById(R.id.ratingBarNonAnonymous);
        TextView anonRatingNum = view.findViewById(R.id.anonymous_rating_number);
        TextView nonanonRatingNum = view.findViewById(R.id.non_anonymous_rating_number);

        anonRatingBar.setRating(AnonymousRating);
        nonanonRatingBar.setRating(NonanonymousRating);
        anonRatingNum.setText(String.valueOf(AnonymousRating));
        nonanonRatingNum.setText(String.valueOf(NonanonymousRating));
        name.setText(Name);
        code.setText(Code);
        professor.setText(Professor);
        description.setText(Description);
        universityName.setText(UniversityName);

        // Set the course's image and banner if they exist.
        if (Image != null && !Image.equals("")) {
            Bitmap imageBitmap = ImageHandler.decodeImageString(Image);
            image.setImageBitmap(imageBitmap);
        }
        if (Banner != null && !Banner.equals("")) {
            Bitmap bannerBitmap = ImageHandler.decodeImageString(Banner);
            banner.setImageBitmap(bannerBitmap);
        }

        // Linear layout for putting reviews's views.
        LinearLayout reviews = view.findViewById(R.id.reviewList);
        loadInitialReviews(reviews, inflater, container);

        TextView yourReview = view.findViewById(R.id.yourReview);
        TextView everyone = view.findViewById(R.id.everyone);
        everyone.setTypeface(Typeface.DEFAULT_BOLD);
        yourReview.setTypeface(Typeface.DEFAULT);

        // Set the on click listeners for the "Your Review" and "Everyone" buttons to load the respective reviews.

        yourReview.setOnClickListener(v -> {
            if (yourReview.getTypeface() == Typeface.DEFAULT) {
                loadYourReview(reviews, inflater, container);
                yourReview.setTypeface(Typeface.DEFAULT_BOLD);
                everyone.setTypeface(Typeface.DEFAULT);
            }
        });

        everyone.setOnClickListener(v -> {
            if (everyone.getTypeface() == Typeface.DEFAULT) {
                loadInitialReviews(reviews, inflater, container);
                everyone.setTypeface(Typeface.DEFAULT_BOLD);
                yourReview.setTypeface(Typeface.DEFAULT);
            }
        });

        // Linear layout for putting friend reviews's views.
        LinearLayout friendReviewsWrapper = view.findViewById(R.id.friendReviewsWrapper);

        if (FriendReviews == null || FriendReviews.size() == 0) {
            friendReviewsWrapper.setVisibility(View.GONE);
        } else {
            friendReviewsWrapper.setVisibility(View.VISIBLE);

            LinearLayout friendReviewsLayout = view.findViewById(R.id.friendReviews);

            for (FriendReviewModel review : FriendReviews) {
                friendReviewsLayout.addView(review.createView(inflater, container, activity));
            }
        }

        String attachedUniversity = JWTValidation.getTokenProperty(activity, "university");

        LinearLayout reviewHeaders = view.findViewById(R.id.reviewHeaders);

        // If the user is not attached to the university, hide the review headers.
        if (attachedUniversity == null || UniversityId != Integer.parseInt(attachedUniversity)) {
            reviewHeaders.setVisibility(View.GONE);
        } else {
            reviewHeaders.setVisibility((View.VISIBLE));

            Button writeReview = view.findViewById(R.id.writeReview);

            // If the user has not written a review, set the button to write a review, else set it to edit the review.
            if (YourReview == null) {
                yourReview.setVisibility(View.INVISIBLE);
                writeReview.setText("Write Review");
                writeReview.setOnClickListener(v -> ((IActivity) activity).replaceFragment(
                        new WriteReviewFragment(new CourseModel(Id, Name, Code, Image)), true));
            } else {
                yourReview.setVisibility(View.VISIBLE);
                writeReview.setText("Edit Review");
                writeReview.setOnClickListener(v -> ((IActivity) activity).replaceFragment(
                        new WriteReviewFragment(new CourseModel(Id, Name, Code, Image),
                                new ReviewModel(YourReview.Id, YourReview.Rating, YourReview.Comment, YourReview.IsAnonymous, Id)),
                        true));
            }
        }

        return view;
    }

    /**
     * Loads the user's review.
     *
     * @param reviews    The linear layout for the reviews.
     * @param inflater   The layout inflater.
     * @param container  The parent layout.
     */
    private void loadYourReview(LinearLayout reviews, LayoutInflater inflater, ViewGroup container) {
        reviews.removeAllViews();
        reviews.addView(YourReview.createView(inflater, container));
    }

    /**
     * Loads the initial reviews.
     *
     * @param reviews    The linear layout for the reviews.
     * @param inflater   The layout inflater.
     * @param container  The parent layout.
     */
    private void loadInitialReviews(LinearLayout reviews, LayoutInflater inflater, ViewGroup container) {
        reviews.removeAllViews();
        for (ReviewListItem i : Reviews) {
            reviews.addView(i.createView(inflater, container));
        }
    }
}
