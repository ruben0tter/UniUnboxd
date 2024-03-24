package com.example.uniunboxd.models.student;

import android.app.Activity;
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
import androidx.fragment.app.FragmentManager;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.utilities.ImageHandler;
import com.example.uniunboxd.utilities.JWTValidation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.HttpURLConnection;
import java.util.List;

public class StudentProfileModel {
    public final int Id;
    public String Name;
    public String Image;
    public String UniversityName;
    public List<StudentListItem> Following;
    public List<StudentListItem> Followers;
    public List<StudentReviewListItem> Reviews;
    private boolean isFollowing = false;

    @JsonCreator
    public StudentProfileModel(@JsonProperty("id") int Id, @JsonProperty("name") String Name, @JsonProperty("universityName") String UniversityName,
                               @JsonProperty("profilePic") String Image, @JsonProperty("following") List<StudentListItem> Following,
                               @JsonProperty("followers") List<StudentListItem> Followers, @JsonProperty("reviews") List<StudentReviewListItem> Reviews) {
        this.Id = Id;
        this.Name = Name;
        this.Image = Image;
        this.Following = Following;
        this.Followers = Followers;
        this.Reviews = Reviews;
        this.UniversityName = UniversityName;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Fragment f){
        View view = inflater.inflate(R.layout.fragment_student_profile_page, container, false);

        ImageView image = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);
        TextView universityName = view.findViewById(R.id.universityName);
        Button followAction = view.findViewById(R.id.followActionButton);

        LinearLayout following = view.findViewById(R.id.listFollowingLinear);
        LinearLayout followers = view.findViewById(R.id.listFollowersLinear);
        LinearLayout reviews = view.findViewById(R.id.listReviewsLinear);

        name.setText(Name);
        universityName.setText("Enrolled at" + UniversityName);

        if(Image != null)
            image.setImageBitmap(ImageHandler.decodeImageString(Image));

        if(!Following.isEmpty())
            for (StudentListItem x : Following) {
                following.addView(x.createView(inflater, container, savedInstanceState, f));
            }

        if(!Followers.isEmpty())
            for (StudentListItem x : Followers) {
                if(x.ID == Id)
                    isFollowing = true;
                followers.addView(x.createView(inflater, container, savedInstanceState, f));
            }

        if(!Reviews.isEmpty())
            for (StudentReviewListItem x : Reviews) {
                reviews.addView(x.createView(inflater, container, savedInstanceState));
            }

        int userId = Integer.parseInt(JWTValidation.getTokenProperty(f.getActivity(), "sub"));

        if(isFollowing) {
            followAction.setText("UNFOLLOW");
        } else {
            followAction.setText("FOLLOW");
        }
        followAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            if(isFollowing) {
                                try {
                                    HttpURLConnection con = UserController.unfollow(Id, f.getActivity());
                                    if (con.getResponseCode() == 200) {
                                        followAction.setText("FOLLOW");
                                        StudentListItem followingUser = Followers.stream().filter(x -> x.ID == Id).findFirst().get();
                                        Followers.remove(followingUser);
                                        followers.removeView(followingUser.createView(inflater, container, savedInstanceState, f));
                                        isFollowing = false;
                                    } else {
                                        Log.d("STUDENT", "" + con.getResponseCode());
                                    }
                                } catch (Exception e) {
                                    Log.e("ERR", e.toString());
                                }
                            }
                            else {
                                try {
                                    HttpURLConnection con = UserController.follow(Id, f.getActivity());
                                    if (con.getResponseCode() == 200) {
                                        followAction.setText("UNFOLLOW");
                                        StudentListItem followingUser = new StudentListItem(Id, Name, Image);
                                        Followers.add(followingUser);
                                        followers.addView(followingUser.createView(inflater, container, savedInstanceState, f));
                                        isFollowing = true;
                                    } else {
                                        Log.d("STUDENT", "" + con.getResponseCode());
                                    }
                                } catch (Exception e) {
                                    Log.e("ERR", e.toString());
                                }
                            }
                        }
                    });
                }
            });
        if (userId == Id)
            followAction.setVisibility(View.GONE);
        return view;
    }
}
