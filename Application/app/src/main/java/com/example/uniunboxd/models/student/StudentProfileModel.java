package com.example.uniunboxd.models.student;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The StudentProfileModel class is a model for a student profile.
 */
public class StudentProfileModel {
    public final int Id;
    public String Name;
    public String Image;
    public String UniversityName;
    public List<StudentListItem> Following;
    public List<StudentListItem> Followers;
    public List<StudentReviewListItem> Reviews;
    public final NotificationSettings NotificationSettings;
    public int VerificationStatus;
    private boolean isFollowing = false;

    /**
     * Constructor for the StudentProfileModel class.
     *
     * @param Id                  The student's ID.
     * @param Name                The student's name.
     * @param UniversityName      The student's university name.
     * @param Image               The student's image.
     * @param Following           The student's following list.
     * @param Followers           The student's followers list.
     * @param Reviews             The student's reviews list.
     * @param NotificationSettings The student's notification settings.
     * @param VerificationStatus   The student's verification status.
     */
    @JsonCreator
    public StudentProfileModel(@JsonProperty("id") int Id, @JsonProperty("name") String Name, @JsonProperty("universityName") String UniversityName,
                               @JsonProperty("profilePic") String Image, @JsonProperty("following") List<StudentListItem> Following,
                               @JsonProperty("followers") List<StudentListItem> Followers, @JsonProperty("reviews") List<StudentReviewListItem> Reviews,
                               @JsonProperty("notificationSettings") NotificationSettings NotificationSettings,
                               @JsonProperty("verificationStatus") int VerificationStatus) {
        this.Id = Id;
        this.Name = Name;
        this.Image = Image;
        this.Following = Following;
        this.Followers = Followers;
        this.Reviews = Reviews;
        this.UniversityName = UniversityName;
        this.NotificationSettings = NotificationSettings;
        this.VerificationStatus = VerificationStatus;
    }

    /**
     * Creates a view for the student profile page.
     *
     * @param inflater  The layout inflater.
     * @param container The parent layout.
     * @param f         The fragment.
     * @return The view for the student profile page.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f) {
        View view = inflater.inflate(R.layout.fragment_student_profile_page, container, false);

        ImageView image = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);
        TextView universityName = view.findViewById(R.id.universityName);
        Button followAction = view.findViewById(R.id.followActionButton);

        // Linear layouts where we will put the users the user follows, their followers, and reviews.
        LinearLayout following = view.findViewById(R.id.listFollowingLinear);
        LinearLayout followers = view.findViewById(R.id.listFollowersLinear);
        LinearLayout reviews = view.findViewById(R.id.listReviewsLinear);

        name.setText(Name);
        universityName.setText("Enrolled at: " + (UniversityName.isEmpty() ?
                "[not verified]"
                : UniversityName));

        // Set the student's image if it exists.
        if (Image != null)
            image.setImageBitmap(ImageHandler.decodeImageString(Image));

        int userId = Integer.parseInt(JWTValidation.getTokenProperty(f.getActivity(), "sub"));

        // Add the users the user follows, their followers, and reviews to the linear layouts.

        if (!Following.isEmpty())
            for (StudentListItem x : Following) {
                following.addView(x.createView(inflater, container, f.getActivity()));
            }

        if (!Followers.isEmpty())
            for (StudentListItem x : Followers) {
                if (x.ID == userId)
                    isFollowing = true;
                followers.addView(x.createView(inflater, container, f.getActivity()));
            }

        if (!Reviews.isEmpty())
            for (StudentReviewListItem x : Reviews) {
                reviews.addView(x.createView(inflater, container, f));
            }


        if (isFollowing) {
            followAction.setText("Unfollow");
        } else {
            followAction.setText("Follow");
        }

        // Set the on click listener for the follow action button.
        followAction.setOnClickListener(v -> AsyncTask.execute(() -> {
            if (isFollowing) {
                try {
                    // Send and unfollow request to the API.
                    UserController.unfollow(Id, f.getActivity());
                    isFollowing = false;
                    StudentListItem followingUser = Followers.stream().filter(x -> x.ID == userId).findFirst().get();
                    Followers.remove(followingUser);

                    // Update the UI.
                    f.getActivity().runOnUiThread(() -> {
                        followAction.setText("Follow");
                        followers.removeAllViews();
                        for (StudentListItem x : Followers) {
                            followers.addView(x.createView(inflater, container, f.getActivity()));
                        }
                        if (Followers.isEmpty()) {
                            view.findViewById(R.id.Followers).setVisibility(View.GONE);
                            view.findViewById(R.id.listFollowers).setVisibility(View.GONE);
                            view.findViewById(R.id.listFollowersLinear).setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                }
            } else {
                try {
                    // Send a follow request to the API.
                    UserController.follow(Id, f.getActivity());
                    isFollowing = true;
                    StudentListItem user = UserController.getStudentListItem(userId, f.getActivity());
                    Followers.add(user);

                    // Update the UI.
                    f.getActivity().runOnUiThread(() -> {
                        followAction.setText("Unfollow");
                        followers.addView(user.createView(inflater, container, f.getActivity()));
                        view.findViewById(R.id.Followers).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.listFollowers).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.listFollowersLinear).setVisibility(View.VISIBLE);
                    });
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                }
            }
        }));
        
        // Hide the follow action button if the user is viewing their own profile.
        if (userId == Id)
            followAction.setVisibility(View.GONE);
        return view;
    }
}
