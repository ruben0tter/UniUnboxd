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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.API.VerificationController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.student.StudentEditModel;
import com.example.uniunboxd.models.student.UniversityNameModel;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.ImageHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class StudentEditFragment extends Fragment {

    private static int CAMERA_CODE = 1;
    private static int IMAGE_PICKER_CODE = 2;
    private static int FILE_PICKER_CODE = 3;

    private ArrayList<String> UniversityNames = new ArrayList<>();
    private StudentEditModel Model;
    private List<byte[]> file = new ArrayList<>();

    private List<UniversityNameModel> Universities;

    public StudentEditFragment(StudentEditModel studentEditModel) {
        Model = studentEditModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 1);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student_profile_page_edit, container, false);

        ImageView image = view.findViewById(R.id.image);
        EditText name = view.findViewById(R.id.name);
        ImageView editImageBtn = view.findViewById(R.id.editImageBtn);
        Button saveChangesBtn = view.findViewById(R.id.saveChanges);
        Button uploadBtn = view.findViewById(R.id.upload);
        Button verifyBtn = view.findViewById(R.id.getVerified);
        AutoCompleteTextView universitySearch = view.findViewById(R.id.universityAutoCompleteTextView);

        AsyncTask.execute(() -> {
            try {
                Universities = UserController.GetUniversities(getActivity());
                for (UniversityNameModel x : Universities) {
                    UniversityNames.add(x.Name);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, UniversityNames);
                (getActivity()).runOnUiThread(() -> universitySearch.setAdapter(arrayAdapter));
            } catch (IOException e) {
                Log.e("ERR", "Could not get universities.");
            }
        });


        name.setText(Model.Name);

        if (Model.Image != null) image.setImageBitmap(ImageHandler.decodeImageString(Model.Image));

        int fileSystemPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (fileSystemPermission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

        PopupMenu dropDownMenu = new PopupMenu(getActivity(), editImageBtn);
        Menu menu = dropDownMenu.getMenu();
        menu.add(0, 0, 0, "Camera");
        menu.add(0, 1, 0, "Files");

        Fragment f = this;
        dropDownMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 0:
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_DENIED) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_CODE);
                    }
                    return true;
                case 1:
                    FileSystemChooser.ChooseImage(f, IMAGE_PICKER_CODE);
                    return true;
            }
            return false;
        });

        editImageBtn.setOnClickListener(v -> dropDownMenu.show());

        saveChangesBtn.setOnClickListener(v -> AsyncTask.execute(() -> {
            try {
                Model.Name = name.getText().toString();
                UpdateNotificationSettings(view);
                HttpURLConnection con = UserController.putStudent(Model, getActivity());
                if (con.getResponseCode() == 200) {
                    ((IActivity) getActivity()).replaceFragment(new StudentProfileFragment(Model.Id), true);
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    StringBuilder st = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null)
                        st.append(line);
                    Log.e("ERR", "" + st);
                }
            } catch (Exception e) {
                Log.e("ERR", e.toString());
            }

        }));

        uploadBtn.setOnClickListener(v -> FileSystemChooser.ChoosePDF(f, 3));

        verifyBtn.setOnClickListener(v -> AsyncTask.execute(() -> {

            try {
                String universityName = universitySearch.getText().toString();
                int id = -1;
                for (UniversityNameModel university : Universities) {
                    if (university.Name.equals(universityName)) {
                        id = university.Id;
                        break;
                    }
                }
                if (id == -1) {
                    return;
                }
                HttpURLConnection con = VerificationController.sendApplication(file, id, getActivity());
                if (con.getResponseCode() != 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    StringBuilder st = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null)
                        st.append(line);
                    Log.e("DEB", "" + st);
                    return;
                }
                Model.VerificationStatus = 1;
                getActivity().runOnUiThread(() -> {
                    view.findViewById(R.id.verification).setVisibility(View.GONE);
                    view.findViewById(R.id.verificationBox).setVisibility(View.GONE);
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));

        SetUpNotificationSettings(view);

        if (Model.VerificationStatus != 0) {
            view.findViewById(R.id.verification).setVisibility(View.GONE);
            view.findViewById(R.id.verificationBox).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = this.getView().findViewById(R.id.image);
        if (requestCode == CAMERA_CODE) {
            Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(cameraImage);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            cameraImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            Model.Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else if (requestCode == FILE_PICKER_CODE) {
            Uri uri = data.getData();
            try {
                //null file, so that it only keeps the most recent.
                if (file.size() > 0)
                    file = new ArrayList<>();
                file.add(FileSystemChooser.readTextFromUri(uri, getActivity()));
            } catch (IOException e) {
                //TODO: deal with this better
                throw new RuntimeException(e);
            }
        } else if (requestCode == IMAGE_PICKER_CODE) {
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

    private void SetUpNotificationSettings(View view) {

        CheckBox followerMail = view.findViewById(R.id.follower_mail);
        CheckBox followerPush = view.findViewById(R.id.follower_push);
        CheckBox replyMail = view.findViewById(R.id.reply_mail);
        CheckBox replyPush = view.findViewById(R.id.reply_push);
        CheckBox activityMail = view.findViewById(R.id.activity_mail);
        CheckBox activityPush = view.findViewById(R.id.activity_push);

        if (Model.NotificationSettings.ReceivesNewFollowerMail)
            followerMail.setChecked(true);

        if (Model.NotificationSettings.ReceivesNewFollowerPush)
            followerPush.setChecked(true);

        if (Model.NotificationSettings.ReceivesNewReplyMail)
            replyMail.setChecked(true);

        if (Model.NotificationSettings.ReceivesNewReplyPush)
            replyPush.setChecked(true);

        if (Model.NotificationSettings.ReceivesFollowersReviewMail)
            activityMail.setChecked(true);

        if (Model.NotificationSettings.ReceivesFollowersReviewPush)
            activityPush.setChecked(true);
    }

    private void UpdateNotificationSettings(View view) {
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
