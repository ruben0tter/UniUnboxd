package com.example.uniunboxd.DTO;

/**
 * The ReviewModel class represents a review for a course.
 * It includes an ID, a rating, a comment, a flag indicating if the review is anonymous, and the ID of the course.
 */
public class ReviewModel {
    /**
     * The unique identifier of the review.
     */
    public int id;

    /**
     * The rating given in the review.
     */
    public double rating;

    /**
     * The comment made in the review.
     */
    public String comment;

    /**
     * A flag indicating if the review is anonymous.
     */
    public boolean isAnonymous;

    /**
     * The ID of the course that the review is for.
     */
    public int courseId;

    /**
     * Constructs a new ReviewModel with the specified rating, comment, anonymity flag, and course ID.
     * The ID of the review is not set in this constructor.
     * @param rating The rating given in the review.
     * @param comment The comment made in the review.
     * @param isAnonymous A flag indicating if the review is anonymous.
     * @param courseId The ID of the course that the review is for.
     */
    public ReviewModel(double rating, String comment, boolean isAnonymous, int courseId) {
        this.rating = rating;
        this.comment = comment;
        this.isAnonymous = isAnonymous;
        this.courseId = courseId;
    }

    /**
     * Constructs a new ReviewModel with the specified ID, rating, comment, anonymity flag, and course ID.
     * @param id The unique identifier of the review.
     * @param rating The rating given in the review.
     * @param comment The comment made in the review.
     * @param isAnonymous A flag indicating if the review is anonymous.
     * @param courseId The ID of the course that the review is for.
     */
    public ReviewModel(int id, double rating, String comment, boolean isAnonymous, int courseId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.isAnonymous = isAnonymous;
        this.courseId = courseId;
    }
}
