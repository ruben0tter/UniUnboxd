package com.example.uniunboxd;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Application {
    public final int ID;
    public final String NAME;
    public final String PFP;
    public final String[] FILES;

    public Application(int id, String name, String pfp, String[] files) {
        ID = id;
        NAME = name;
        PFP = pfp;
        FILES = files;
    }

    public View createView(LayoutInflater inflater, LinearLayout parent, Activity activity) {
        View view = inflater.inflate(R.layout.application_item, null);

        Button btnAccept = view.findViewById(R.id.accept_button);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(activity, "Accepted application.", Toast.LENGTH_LONG).show();
                parent.removeView(view);
            }
        });

        Button btnReject = view.findViewById(R.id.reject_button);
        btnReject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(activity, "Rejected application.", Toast.LENGTH_LONG).show();
                parent.removeView(view);
            }
        });

        ImageView image = view.findViewById(R.id.imageView);

        byte[] decodedString = Base64.decode(PFP, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);

        return view;
    }
}