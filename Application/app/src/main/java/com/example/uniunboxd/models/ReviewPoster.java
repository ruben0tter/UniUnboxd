package com.example.uniunboxd.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ReviewPoster class that represents a review poster.
 */
public class ReviewPoster {
    public int Id;
    public String UserName;
    public String Image;

    /**
     * Constructor for the ReviewPoster class.
     *
     * @param id      The poster's ID.
     * @param userName The poster's username.
     * @param image    The poster's image.
     */
    @JsonCreator
    public ReviewPoster(@JsonProperty("id") int id, @JsonProperty("userName") String userName,@JsonProperty("image") String image) {
        Id = id;
        UserName = userName;
        Image = image;
    }
}
