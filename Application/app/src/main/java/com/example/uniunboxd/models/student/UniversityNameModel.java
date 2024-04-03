package com.example.uniunboxd.models.student;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UniversityNameModel {
    public int Id;
    public String Name;

    @JsonCreator
    public UniversityNameModel(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        Id = id;
        Name = name;
    }
}