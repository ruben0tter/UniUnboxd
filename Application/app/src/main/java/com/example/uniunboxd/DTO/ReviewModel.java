package com.example.uniunboxd.DTO;

public class ReviewModel {
    public int id;
    public double rating;
    public String comment;
    public boolean isAnonymous;
    public int courseId;
    public int studentId;

    public ReviewModel(double rating, String comment, boolean isAnonymous, int courseId, int studentId) {
        this.rating = rating;
        this.comment = comment;
        this.isAnonymous = isAnonymous;
        this.courseId = courseId;
        this.studentId = studentId;
    }
}
