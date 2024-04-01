package com.example.uniunboxd.models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Application {
    public final int ID;
    public final String NAME;
    public final String PFP;
    public final String[] FILES;

    public Application(@JsonProperty("userId") int id, @JsonProperty("name") String name, @JsonProperty("image") String pfp,
                       @JsonProperty("verificationData") String[] files) {
        ID = id;
        NAME = name;
        PFP = pfp;
        FILES = files;
    }

    public View createView(LayoutInflater inflater, LinearLayout parent, Activity activity) {
        View view = inflater.inflate(R.layout.application_item, null);

        Button btnAccept = view.findViewById(R.id.accept_button);
        btnAccept.setOnClickListener(v -> {
            Toast.makeText(activity, "Accepted application.", Toast.LENGTH_SHORT).show();
            AsyncTask.execute(() -> {
                try {
                    VerificationController.resolveApplication(ID, true, (FragmentActivity) activity);
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                    //                throw new RuntimeException("I dont even know");
                }
            });
            parent.removeView(view);
        });

        Button btnReject = view.findViewById(R.id.reject_button);
        btnReject.setOnClickListener(v -> {
            Toast.makeText(activity, "Rejected application.", Toast.LENGTH_LONG).show();
            AsyncTask.execute(() -> {
                try {
                    VerificationController.resolveApplication(ID, false, (FragmentActivity) activity);
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                    //                throw new RuntimeException("I dont even know");
                }
            });
            parent.removeView(view);
        });

        TextView label = view.findViewById(R.id.application_text);
        label.setText(NAME);
        Button btnDocuments = view.findViewById(R.id.view_documents_button);
        btnDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Set to open / download files.
            }
        });

        try {
            ImageView image = view.findViewById(R.id.imageView);

            byte[] decodedString = Base64.decode(PFP, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        } catch (Exception e) {
            // no image
        }

        return view;
    }
}