package com.example.uniunboxd.models;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Application class that represents a user's application for verification.
 */
public class Application {
    public final int ID;
    public final String EMAIL;
    public final String PFP;
    public final String[] FILES;

    /**
     * Constructor for the Application class.
     *
     * @param id    The user's ID.
     * @param email The user's email.
     * @param pfp   The user's profile picture.
     * @param files The user's verification documents.
     */
    public Application(@JsonProperty("userId") int id, @JsonProperty("email") String email, @JsonProperty("image") String pfp,
                       @JsonProperty("verificationData") String[] files) {
        ID = id;
        EMAIL = email;
        PFP = pfp;
        FILES = files;
    }

    /**
     * Creates a view for the application.
     *
     * @param inflater The layout inflater.
     * @param parent   The parent layout.
     * @param activity The activity.
     * @return The view for the application.
     */
    public View createView(LayoutInflater inflater, LinearLayout parent, Activity activity) {
        View view = inflater.inflate(R.layout.application_item, null);

        
        Button btnAccept = view.findViewById(R.id.accept_button);
        btnAccept.setOnClickListener(v -> {
            // Notify the user that the application has been accepted.
            Toast.makeText(activity, "Accepted application.", Toast.LENGTH_SHORT).show();
            AsyncTask.execute(() -> {
                try {
                    // Resolve the application with the API.
                    VerificationController.resolveApplication(ID, true, (FragmentActivity) activity);
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                }
            });
            // Remove the application from the parent layout.
            parent.removeView(view);
        });

        Button btnReject = view.findViewById(R.id.reject_button);
        btnReject.setOnClickListener(v -> {
            // Notify the user that the application has been rejected.
            Toast.makeText(activity, "Rejected application.", Toast.LENGTH_LONG).show();
            AsyncTask.execute(() -> {
                try {
                    // Resolve the application with the API.
                    VerificationController.resolveApplication(ID, false, (FragmentActivity) activity);
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                    //                throw new RuntimeException("I dont even know");
                }
            });
            // Remove the application from the parent layout.
            parent.removeView(view);
        });

        TextView label = view.findViewById(R.id.application_text);
        label.setText(EMAIL);
        Button btnDocuments = view.findViewById(R.id.view_documents_button);

        AtomicInteger docIndex = new AtomicInteger();

        btnDocuments.setOnClickListener(v -> {
            // Open the verification documents.
            String file_b64 = FILES[docIndex.getAndIncrement() % FILES.length];
            byte[] file = Base64.decode(file_b64, Base64.DEFAULT);

            File outputDir = activity.getCacheDir();
            File outputFile;
            try {
                outputFile = File.createTempFile("verificationDoc", ".pdf", outputDir);
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(file);
                outputStream.close();
            } catch (IOException e) {
                Log.e("VERIF", "Failed to write file");
                return;
            }

            // Open the file with a PDF viewer.
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", outputFile);
            Intent shareIntent = new Intent(Intent.ACTION_VIEW, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(shareIntent, null));
        });

        try {
            ImageView image = view.findViewById(R.id.imageView);

            // Decode the profile picture.
            byte[] decodedString = Base64.decode(PFP, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        } catch (Exception e) {
            // There was an error decoding the image or no image was provided.
        }

        return view;
    }
}