package com.example.uniunboxd.models.review;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CourseHeader class that represents a course header.
 */
public class CourseHeader {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Image;
    public final String Banner;

    /**
     * Constructor for the CourseHeader class.
     *
     * @param id     The course's ID.
     * @param name   The course's name.
     * @param code   The course's code.
     * @param image  The course's image.
     * @param banner The course's banner.
     */
    @JsonCreator
    public CourseHeader(@JsonProperty("id") int id, @JsonProperty("name") String name,
                        @JsonProperty("code") String code, @JsonProperty("image") String image,
                        @JsonProperty("banner") String banner) {
        Id = id;
        Name = name;
        Code = code;
        Image = image;
        Banner = banner;
    }
}
