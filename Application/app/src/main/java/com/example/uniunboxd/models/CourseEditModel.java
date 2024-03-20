package com.example.uniunboxd.models;

public class CourseEditModel {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String Description;

    public final String Professor;

    public final String Image;
    public final String Banner;

    public CourseEditModel(int id, String name, String code, String description, String professor, String image, String banner) {
        Id = id;
        Name = name;
        Code = code;
        Description = description;
        Professor = professor;
        Image = image;
        Banner = banner;
    }

}

