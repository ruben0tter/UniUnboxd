package com.example.uniunboxd.models.student;

import android.os.AsyncTask;
import android.os.Bundle;
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

    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Fragment f) {
        View view = inflater.inflate(R.layout.fragment_student_profile_page, container, false);

        ImageView image = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);
        TextView universityName = view.findViewById(R.id.universityName);
        Button followAction = view.findViewById(R.id.followActionButton);

        LinearLayout following = view.findViewById(R.id.listFollowingLinear);
        LinearLayout followers = view.findViewById(R.id.listFollowersLinear);
        LinearLayout reviews = view.findViewById(R.id.listReviewsLinear);

        name.setText(Name);
        universityName.setText("Enrolled at: " + (UniversityName.isEmpty() ?
                "[not verified]"
                : UniversityName));

        if (Image != null)
            image.setImageBitmap(ImageHandler.decodeImageString(Image));

        int userId = Integer.parseInt(JWTValidation.getTokenProperty(f.getActivity(), "sub"));
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
                reviews.addView(x.createView(inflater, container, savedInstanceState, f));
            }


        if (isFollowing) {
            followAction.setText("Unfollow");
        } else {
            followAction.setText("Follow");
        }
        followAction.setOnClickListener(v -> AsyncTask.execute(() -> {
            if (isFollowing) {
                try {
                    UserController.unfollow(Id, f.getActivity());
                    isFollowing = false;
                    StudentListItem followingUser = Followers.stream().filter(x -> x.ID == userId).findFirst().get();
                    Followers.remove(followingUser);
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
                    UserController.follow(Id, f.getActivity());
                    isFollowing = true;
                    StudentListItem user = UserController.getStudentListItem(userId, f.getActivity());
                    Followers.add(user);
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
        if (userId == Id)
            followAction.setVisibility(View.GONE);
        return view;
    }
}
