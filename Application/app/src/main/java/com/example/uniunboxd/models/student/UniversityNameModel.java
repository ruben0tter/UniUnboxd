package com.example.uniunboxd.models.student;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UniversityNameModel {
    public int Id;
    public String Name;

    /**
     * Constructor for the UniversityNameModel class.
     *
     * @param id   The university's ID.
     * @param name The university's name.
     */
    @JsonCreator
    public UniversityNameModel(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        Id = id;
        Name = name;
    }
}