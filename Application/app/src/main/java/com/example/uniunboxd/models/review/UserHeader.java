package com.example.uniunboxd.models.review;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserHeader {
    public final int Id;
    public final String Name;
    public final String Image;

    @JsonCreator
    public UserHeader(@JsonProperty("id") int id, @JsonProperty("name") String name,
                      @JsonProperty("image") String image) {
        Id = id;
        Name = name;
        Image = image;
    }
}
