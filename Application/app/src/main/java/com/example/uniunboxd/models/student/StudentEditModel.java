package com.example.uniunboxd.models.student;

public class StudentEditModel {
    public final int Id;
    public String Name;
    public String Image;


    public StudentEditModel(StudentProfileModel student) {
        Id = student.Id;
        Image = student.Image;
        Name = student.Name;
    }
}
