package com.example.uniunboxd.DTO;

public class ReviewModel {
    public int id;
    public double rating;
    public String comment;
    public boolean isAnonymous;
    public int courseId;

    public ReviewModel(double rating, String comment, boolean isAnonymous, int courseId) {
        this.rating = rating;
        this.comment = comment;
        this.isAnonymous = isAnonymous;
        this.courseId = courseId;
    }

    public ReviewModel(int id, double rating, String comment, boolean isAnonymous, int courseId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.isAnonymous = isAnonymous;
        this.courseId = courseId;
    }
}
