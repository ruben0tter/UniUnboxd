package com.example.uniunboxd.fragments.university;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.uniunboxd.API.CourseController;
import com.example.uniunboxd.API.UserController;
import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.IActivity;
import com.example.uniunboxd.models.course.AssignedProfessorModel;
import com.example.uniunboxd.models.course.CourseCreationModel;
import com.example.uniunboxd.models.course.CourseEditModel;
import com.example.uniunboxd.utilities.FileSystemChooser;
import com.example.uniunboxd.utilities.ImageHandler;
import com.example.uniunboxd.utilities.JWTValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateCourseFragment class that represents the create course screen.
 */
public class CreateCourseFragment extends Fragment implements View.OnClickListener {
    private LayoutInflater inflater;
    private ViewGroup container;
    private Bundle savedInstanceState;
    private final int imageCode = 1;
    private final int bannerCode = 2;

    // Define data variables
    private String imageEnc = null;
    private String bannerEnc = null;
    protected final CourseEditModel Course;
    public List<AssignedProfessorModel> assignedProfessors = new ArrayList<>();

    /**
     * Necessary empty constructor.
     */
    public CreateCourseFragment() {
        Course = null;
    }

    /**
     * Constructor for the CreateCourseFragment class.
     *
     * @param course The course edit model.
     */
    public CreateCourseFragment(CourseEditModel course) {
        this.Course = course;
        imageEnc = Course.Image;
        bannerEnc = Course.Banner;
    }

    /**
     * Creates the view for the create course fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The parent layout.
     * @param savedInstanceState The saved instance state.
     * @return The view for the create course fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;
        View view = inflater.inflate(R.layout.fragment_course_page_setup, container, false);

        // Define layout variables
        Button saveChangesBtn = view.findViewById(R.id.saveChanges);
        ImageButton imageBtn = view.findViewById(R.id.courseImage_edit);
        ImageButton bannerBtn = view.findViewById(R.id.courseBanner_edit);
        Button deleteBtn = view.findViewById(R.id.deleteButton);
        ImageButton searchBtn = view.findViewById(R.id.searchButton);

        CreateCourseFragment f = this;
        // If the course is not null, set the course information.
        if (Course != null) {
            // Set the course information.
            ImageView image = view.findViewById(R.id.courseImage_image_courseSetup);
            ImageView banner = view.findViewById(R.id.courseBanner_image_courseSetup);
            EditText name = view.findViewById(R.id.courseName_courseName_edit);
            EditText code = view.findViewById(R.id.courseName_courseCode_edit);
            EditText description = view.findViewById(R.id.courseDescription_edit);
            EditText professor = view.findViewById(R.id.courseName_courseDescription_edit);

            if (Course.Image != null)
                image.setImageBitmap(ImageHandler.decodeImageString(Course.Image));
            if (Course.Banner != null)
                banner.setImageBitmap(ImageHandler.decodeImageString(Course.Banner));
            name.setText(Course.Name);
            code.setText(Course.Code);
            description.setText(Course.Description);
            professor.setText(Course.Professor);
        }
        // Set on click listeners for the buttons.
        saveChangesBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        bannerBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

        // If the user is a university, get the assigned professors.
            LinearLayout assignedProfessorsList = view.findViewById(R.id.assignedProfessorsList);
            AsyncTask.execute(() -> {
                try {
                    if (Course != null) {
                        // Get the assigned professors for the course.
                        assignedProfessors = UserController.getAssignedProfessors(Course.Id, getActivity());
                        getActivity().runOnUiThread(() -> {
                            // Add the assigned professors to the assigned professors list.
                            for (AssignedProfessorModel x : assignedProfessors) {
                                View v = x.CreateView(inflater, container, f);
                                if (JWTValidation.getTokenProperty(this.getActivity(), "typ").equals("University")) {
                                    assignedProfessorsList.addView(v);
                                }
                                Course.AssignedProfessors.add(x);
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e("ERR", "Could not get assigned professors. Error: " + e);
                }
            });
            deleteBtn.setOnClickListener(this);
        if (!JWTValidation.getTokenProperty(this.getActivity(), "typ").equals("University")) {
            // If the user is not a university, hide the delete button and the search button.
            view.findViewById(R.id.deleteWrapper).setVisibility(View.GONE);
            view.findViewById(R.id.searchButton).setVisibility(View.GONE);
            view.findViewById(R.id.SelectedProf).setVisibility(View.GONE);
            view.findViewById(R.id.selectedProfessorLabel).setVisibility(View.GONE);
        }
        return view;
    }

    /**
     * Handles the on click events for the create course fragment.
     *
     * @param view The view.
     */
    @Override
    public void onClick(View view) {
        Fragment f = FragmentManager.findFragment(view);
        int id = view.getId();
        if (id == R.id.saveChanges) {
            saveChangesButtonLogic(view, f);
        } else if (id == R.id.courseImage_edit) {
            FileSystemChooser.ChooseImage(f, imageCode);
        } else if (id == R.id.courseBanner_edit) {
            FileSystemChooser.ChooseImage(f, bannerCode);
        } else if (id == R.id.deleteButton) {
            String message = Course != null ? "Confirm Deleting Course?" : "Confirm Cancel Creating Course?";
            buildConfirmationPopup(message);
        } else if (id == R.id.searchButton) {
            searchButtonLogic(view, f);
        }
    }

    /**
     * Handles the search button logic.
     * @param view The view.
     * @param f The fragment.
     */
    private void searchButtonLogic(View view, Fragment f) {
        LinearLayout layout = (LinearLayout) view.getParent();
        EditText professorToAssign = layout.findViewById(R.id.SelectedProf);
        String email = professorToAssign.getText().toString();
        AsyncTask.execute(() -> {
            try {
                // Get the professor to assign by their email through the user controller in the API.
                AssignedProfessorModel assignedProfessorModel = UserController.getAssignedProfessorByEmail(email, getActivity());
                if (assignedProfessorModel == null) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Could not find a professor user with this email.", Toast.LENGTH_SHORT).show());
                    return;
                }
                // If the professor is already assigned, show a toast message.
                getActivity().runOnUiThread(() -> {
                    if (assignedProfessors.stream().anyMatch(professorModel -> professorModel.Id == assignedProfessorModel.Id)) {
                        Toast.makeText(getActivity(), "Professor is already assigned.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    assignedProfessors.add(assignedProfessorModel);
                    // Add the assigned professor to the assigned professors list.
                    LinearLayout assignedProfessorsList = ((LinearLayout) layout.getParent()).findViewById(R.id.assignedProfessorsList);
                    assignedProfessorsList.addView(assignedProfessorModel.CreateView(inflater, container, (CreateCourseFragment) f));
                });
            } catch (IOException e) {
                Log.e("ERR", "Could not get assigned professor model.");
            }
        });
        professorToAssign.setText("");
    }

    /**
     * Handles the save changes button logic.
     *
     * @param view The view.
     * @param f    The fragment.
     */
    private void saveChangesButtonLogic(View view, Fragment f) {
        // Get the course information.
        LinearLayout layout = (LinearLayout) view.getParent();
        EditText name = layout.findViewById(R.id.courseName_courseName_edit);
        EditText code = layout.findViewById(R.id.courseName_courseCode_edit);
        EditText description = layout.findViewById(R.id.courseDescription_edit);
        EditText professor = layout.findViewById(R.id.courseName_courseDescription_edit);

        // If the course name, code, description, or professor is empty, show a toast message.
        AsyncTask.execute(() -> {
            int universityId = Integer.parseInt(JWTValidation.getTokenProperty(getActivity(), "sub"));
            if (Course == null) {
                CourseCreationModel course = new CourseCreationModel(name.getText().toString(),
                        code.getText().toString(), description.getText().toString(),
                        professor.getText().toString(), imageEnc, bannerEnc, universityId, assignedProfessors);
                try {
                    CourseController.postCourse(course, getActivity());
                    ((IActivity) f.getActivity()).replaceFragment(new UniversityHomeFragment(), true);
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                }
                return;
            }
            // If the course is not null, update the course.
            CourseEditModel course = new CourseEditModel(Course.Id, name.getText().toString(),
                    code.getText().toString(), description.getText().toString(),
                    professor.getText().toString(), imageEnc, bannerEnc, assignedProfessors);
            try {
                CourseController.putCourse(course, getActivity());
                ((IActivity) f.getActivity()).replaceFragment(new CourseFragment(Course.Id), true);
            } catch (Exception e) {
                Log.e("ERR", e.toString());
            }
        });
    }

    /**
     * Builds the confirmation popup.
     *
     * @param message The message.
     */
    private void buildConfirmationPopup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", (dialog, which) -> deleteCourseLogic());
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());

        // Create and show the alert dialog.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Deletes the course logic.
     */
    private void deleteCourseLogic() {
        if (Course != null) {
            AsyncTask.execute(() -> {
                try {
                    CourseController.deleteCourse(Course.Id, getActivity());
                } catch (Exception e) {
                    Log.e("ERR", e.toString());
                }
            });
        }
        ((IActivity) getActivity()).replaceFragment(new UniversityHomeFragment(), true);
    }

    /**
     * Handles the on activity result for the create course fragment.
     *
     * @param requestCode The request code.
     * @param resultCode  The result code.
     * @param data        The data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = null;
        if (requestCode == imageCode) {
            image = this.getView().findViewById(R.id.courseImage_image_courseSetup);
        } else if (requestCode == bannerCode) {
            image = this.getView().findViewById(R.id.courseBanner_image_courseSetup);
        }
        Uri uri = data.getData();

        byte[] bitmapdata = null;
        try {
            bitmapdata = FileSystemChooser.readTextFromUri(uri, getActivity());
        } catch (IOException e) {
            Log.e("ERR", e.toString());
        }

        encodeImageOrBanner(bitmapdata, requestCode);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        image.setImageBitmap(bitmap);
    }

    /**
     * Encodes the image or banner.
     *
     * @param data The data.
     * @param code The code.
     */
    public void encodeImageOrBanner(byte[] data, int code) {
        if (code == imageCode) {
            imageEnc = Base64.encodeToString(data, Base64.DEFAULT);
        } else if (code == bannerCode) {
            bannerEnc = Base64.encodeToString(data, Base64.DEFAULT);
        }
    }

    /**
     * Removes the assigned professor.
     *
     * @param assignedProfessorModel The assigned professor model.
     * @param v                      The view.
     */
    public void RemoveAssignedProfessor(AssignedProfessorModel assignedProfessorModel, View v) {
        LinearLayout linearLayout = getView().findViewById(R.id.assignedProfessorsList);
        linearLayout.removeView(v);
        assignedProfessors.remove(assignedProfessorModel);
    }
}
