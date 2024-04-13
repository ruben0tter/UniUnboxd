package com.example.uniunboxd.models.course;

import java.util.ArrayList;
import java.util.List;

/**
 * The CourseCreationModel class is used to represent a course creation model.
 */
public class CourseCreationModel {
    public final String Name;
    public final String Code;
    public final String Description;

    public final String Professor;

    public final String Image;
    public final String Banner;
    public final int UniversityID;

    
    public List<AssignedProfessorModel> AssignedProfessors;

    /**
     * Constructor for the CourseCreationModel class.
     *
     * @param name               The course's name.
     * @param code               The course's code.
     * @param description        The course's description.
     * @param professor          The course's professor.
     * @param image              The course's image.
     * @param banner             The course's banner.
     * @param universityID       The course's university ID.
     * @param assignedProfessors The course's assigned professors.
     */
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
