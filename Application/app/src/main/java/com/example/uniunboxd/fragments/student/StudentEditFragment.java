package com.example.uniunboxd.fragments.student;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.ImageHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class StudentEditFragment extends Fragment {

    private StudentEditModel Model;

    public StudentEditFragment(StudentEditModel studentEditModel) {
        Model = studentEditModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student_profile_page_edit, container, false);

        ImageView image = view.findViewById(R.id.image);
        EditText name = view.findViewById(R.id.name);
        ImageView editImageBtn = view.findViewById(R.id.editImageBtn);
        Button saveChangesBtn = view.findViewById(R.id.saveChanges);

        name.setText(Model.Name);

        if (Model.Image != null) image.setImageBitmap(ImageHandler.decodeImageString(Model.Image));

        int fileSystemPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (fileSystemPermission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);


        Fragment f = this;
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 1);

                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_DENIED)
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                }
//                if(fileSystemPermission != PackageManager.PERMISSION_GRANTED){
//                    FileSystemChooser.ChooseImage(f, 7);
//                }
            }
        });

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Model.Name = name.getText().toString();
                            UpdateNotificationSettings(view);
                            HttpURLConnection con = UserController.putStudent(Model, getActivity());
                            if (con.getResponseCode() == 200) {
                                ((IActivity) getActivity()).replaceFragment(new StudentProfileFragment(Model.Id));
                            } else {
                                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                                StringBuilder st = new StringBuilder();
                                String line;
                                while((line = br.readLine()) != null)
                                    st.append(line);
                                Log.e("ERR", "" + st);
                            }
                        } catch (Exception e) {
                            Log.e("ERR", e.toString());
                        }

                    }
                });
            }
        });

        SetUpNotificationSettings(view);


        //TODO: set verification
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = this.getView().findViewById(R.id.image);
        if (requestCode == 1) {
            Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(cameraImage);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            cameraImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            Model.Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            Uri uri = data.getData();

            byte[] bitmapdata = null;
            try {
                bitmapdata = FileSystemChooser.readTextFromUri(uri, getActivity());
            } catch (IOException e) {
                Log.e("ERR", e.toString());
            }
            Model.Image = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            image.setImageBitmap(bitmap);
        }
    }

    private void SetUpNotificationSettings(View view){

        CheckBox followerMail = view.findViewById(R.id.follower_mail);
        CheckBox followerPush = view.findViewById(R.id.follower_push);
        CheckBox replyMail = view.findViewById(R.id.reply_mail);
        CheckBox replyPush = view.findViewById(R.id.reply_push);
        CheckBox activityMail = view.findViewById(R.id.activity_mail);
        CheckBox activityPush = view.findViewById(R.id.activity_push);

        if(Model.NotificationSettings.ReceivesNewFollowerMail)
            followerMail.setChecked(true);

        if(Model.NotificationSettings.ReceivesNewFollowerPush)
            followerPush.setChecked(true);

        if(Model.NotificationSettings.ReceivesNewReplyMail)
            replyMail.setChecked(true);

        if(Model.NotificationSettings.ReceivesNewReplyPush)
            replyPush.setChecked(true);

        if(Model.NotificationSettings.ReceivesFollowersReviewMail)
            activityMail.setChecked(true);

        if(Model.NotificationSettings.ReceivesFollowersReviewPush)
            activityPush.setChecked(true);
    }

    private void UpdateNotificationSettings(View view){
        CheckBox followerMail = view.findViewById(R.id.follower_mail);
        CheckBox followerPush = view.findViewById(R.id.follower_push);
        CheckBox replyMail = view.findViewById(R.id.reply_mail);
        CheckBox replyPush = view.findViewById(R.id.reply_push);
        CheckBox activityMail = view.findViewById(R.id.activity_mail);
        CheckBox activityPush = view.findViewById(R.id.activity_push);

        Model.NotificationSettings.ReceivesFollowersReviewMail = activityMail.isChecked();
        Model.NotificationSettings.ReceivesFollowersReviewPush = activityPush.isChecked();
        Model.NotificationSettings.ReceivesNewReplyMail = replyMail.isChecked();
        Model.NotificationSettings.ReceivesNewReplyPush = replyPush.isChecked();
        Model.NotificationSettings.ReceivesNewFollowerMail = followerMail.isChecked();
        Model.NotificationSettings.ReceivesNewFollowerPush = followerPush.isChecked();
    }
}
