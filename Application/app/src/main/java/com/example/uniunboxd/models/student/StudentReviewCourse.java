package com.example.uniunboxd.models.student;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentReviewCourse {
    @JsonProperty(required = true)
    public final int ID;
    @JsonProperty(required = true)
    public final String Name;
    @JsonProperty(required = true)
    public final String Code;
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
