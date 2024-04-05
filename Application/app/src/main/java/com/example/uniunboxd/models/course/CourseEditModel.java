package com.example.uniunboxd.models.course;

import java.util.ArrayList;
import java.util.List;

public class CourseEditModel {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Description;

    public final String Professor;

    public final String Image;
    public final String Banner;
    public List<AssignedProfessorModel> AssignedProfessors;

    /**
     * Constructor for the CourseEditModel class.
     *
     * @param id                 The course's ID.
     * @param name               The course's name.
     * @param code               The course's code.
     * @param description        The course's description.
     * @param professor          The course's professor.
     * @param image              The course's image.
     * @param banner             The course's banner.
     * @param assignedProfessors The course's assigned professors.
     */
    public CourseEditModel(int id, String name, String code,
                           String description, String professor,
                           String image, String banner, List<AssignedProfessorModel> assignedProfessors) {
        Id = id;
        Name = name;
        Code = code;
        Description = description;
        Professor = professor;
        Image = image;
        Banner = banner;
        AssignedProfessors = assignedProfessors;
    }

}

