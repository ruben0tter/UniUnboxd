package com.example.uniunboxd.models.review;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserHeader class that represents a user header.
 */
public class UserHeader {
    public final int Id;
    public final String Name;
    public final String Image;

    /**
     * Constructor for the UserHeader class.
     *
     * @param id    The user's ID.
     * @param name  The user's name.
     * @param image The user's image.
     */
    @JsonCreator
    public UserHeader(@JsonProperty("id") int id, @JsonProperty("name") String name,
                      @JsonProperty("image") String image) {
        Id = id;
        Name = name;
        Image = image;
    }
}
