package com.example.uniunboxd.models.student;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * StudentReviewCourse class that represents a course for a student review.
*/
public class StudentReviewCourse {
    // The course's ID.
    @JsonProperty(required = true)
    public final int ID;
    // The course's name.
    @JsonProperty(required = true)
    public final String Name;
    // The course's code.
    @JsonProperty(required = true)
    public final String Code;
    // The course's image.
    public final String Image;

    @JsonCreator
    public StudentReviewCourse(@JsonProperty("id") int id, @JsonProperty("name") String name,
                              @JsonProperty("code") String code, @JsonProperty("image") String image) {
        this.ID = id;
        this.Name = name;
        this.Code = code;
        this.Image = image;
    }
}
