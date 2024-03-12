package com.example.uniunboxd.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewPoster {
    public int Id;
    public String UserName;
    public String Image;

    @JsonCreator
    public ReviewPoster(@JsonProperty("id") int id, @JsonProperty("userName") String userName,@JsonProperty("image") String image) {
        Id = id;
        UserName = userName;
        Image = image;
    }
}
