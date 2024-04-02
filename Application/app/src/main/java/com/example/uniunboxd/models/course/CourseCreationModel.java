package com.example.uniunboxd.models.course;

import java.util.ArrayList;
import java.util.List;

public class CourseCreationModel {
    public final String Name;
    public final String Code;
    public final String Description;

    public final String Professor;

    public final String Image;
    public final String Banner;
    public final int UniversityID;

    public List<AssignedProfessorModel> AssignedProfessors;

    public CourseCreationModel(String name, String code, String description, String professor, String image, String banner, int universityID, List<AssignedProfessorModel> assignedProfessors) {
        Name = name;
        Code = code;
        Description = description;
        Professor = professor;
        Image = image;
        Banner = banner;
        UniversityID = universityID;
        AssignedProfessors = assignedProfessors;
    }
}
