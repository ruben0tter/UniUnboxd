package com.example.uniunboxd.DTO;

/**
 * The ReplyModel class represents a reply to a review.
 * It includes the text of the reply and the ID of the review to which it is replying.
 */
public class ReplyModel {
    /**
     * The text of the reply.
     */
    public final String text;

    /**
     * The ID of the review to which this reply is made.
     */
    public final int reviewId;

    /**
     * Constructs a new ReplyModel with the specified text and review ID.
     * @param text The text of the reply.
     * @param reviewId The ID of the review to which this reply is made.
     */
    public ReplyModel(String text, int reviewId) {
        this.text = text;
        this.reviewId = reviewId;
    }
}
