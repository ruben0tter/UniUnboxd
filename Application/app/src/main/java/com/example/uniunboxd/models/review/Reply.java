package com.example.uniunboxd.models.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.fragments.student.StudentProfileFragment;
import com.example.uniunboxd.utilities.ImageHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Reply implements View.OnClickListener {
    public final String Text;
    public final UserHeader User;
    private Fragment fragment;

    @JsonCreator
    public Reply(@JsonProperty("text") String text,
                 @JsonProperty("userHeader") UserHeader user) {
        Text = text;
        User = user;
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f, boolean isLastReply) {
        View view =  inflater.inflate(R.layout.reply, container, false);

        ImageView image = view.findViewById(R.id.replyImage);
        image.setOnClickListener(this);
        TextView user = view.findViewById(R.id.replyUser);
        user.setOnClickListener(this);
        TextView text = view.findViewById(R.id.replyText);

        if(User.Image != null) {
            image.setImageBitmap(ImageHandler.decodeImageString(User.Image));
        }
        user.setText(User.Name);
        text.setText(Text);

        if(isLastReply) {
            View replyDivider = view.findViewById(R.id.replyDivider);
            replyDivider.setVisibility(View.GONE);
        }

        fragment = f;

        return view;
    }

    @Override
    public void onClick(View v) {
        ((IActivity) fragment.getActivity()).replaceFragment(new StudentProfileFragment(User.Id));
    }
}
