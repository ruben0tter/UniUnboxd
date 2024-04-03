package com.example.uniunboxd.models.review;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseHeader {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Image;
    public final String Banner;

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
