package com.example.uniunboxd.DTO;

/**
 * The FlagReviewModel class represents a model for flagging a review.
 * It includes a review ID and a message explaining the reason for flagging.
 */
public class FlagReviewModel {
    /**
     * The unique identifier of the review being flagged.
     */
    public final int reviewId;

    /**
     * The message explaining the reason for flagging the review.
     */
    public final String message;

    /**
     * Constructs a new FlagReviewModel with the specified review ID and message.
     * @param reviewId The unique identifier of the review being flagged.
     * @param message The message explaining the reason for flagging the review.
     */
    public FlagReviewModel(int reviewId, String message) {
        this.reviewId = reviewId;
        this.message = message;
    }
}
