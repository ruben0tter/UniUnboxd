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

/**
 * Reply class that represents a reply.
 */
public class Reply implements View.OnClickListener {
    public final String Text;
    public final UserHeader User;
    private Fragment fragment;

    /**
     * Constructor for the Reply class.
     *
     * @param text The reply's text.
     * @param user The reply's user.
     */
    @JsonCreator
    public Reply(@JsonProperty("text") String text,
                 @JsonProperty("userHeader") UserHeader user) {
        Text = text;
        User = user;
    }

    /**
     * Creates a view for the reply.
     *
     * @param inflater     The layout inflater.
     * @param container    The parent layout.
     * @param f            The fragment.
     * @param isLastReply  Whether the reply is the last reply.
     * @return The view for the reply.
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Fragment f, boolean isLastReply) {
        View view = inflater.inflate(R.layout.reply, container, false);

        ImageView image = view.findViewById(R.id.replyImage);
        image.setOnClickListener(this);
        TextView user = view.findViewById(R.id.replyUser);
        user.setOnClickListener(this);
        TextView text = view.findViewById(R.id.replyText);

        // Set the user image if it exists.
        if (User.Image != null) {
            image.setImageBitmap(ImageHandler.decodeImageString(User.Image));
        }
        user.setText(User.Name);
        text.setText(Text);

        // Hide the reply divider if it is the last reply.
        if (isLastReply) {
            View replyDivider = view.findViewById(R.id.replyDivider);
            replyDivider.setVisibility(View.GONE);
        }

        fragment = f;

        return view;
    }

    /**
     * On click listener for the reply.
     *
     * @param v The view.
     */
    @Override
    public void onClick(View v) {
        // Go to the user's profile.
        ((IActivity) fragment.getActivity()).replaceFragment(new StudentProfileFragment(User.Id), true);
    }
}
