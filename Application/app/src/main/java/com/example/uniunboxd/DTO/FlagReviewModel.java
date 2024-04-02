package com.example.uniunboxd.DTO;

public class FlagReviewModel {
    public final int reviewId;
    public final String message;

    public FlagReviewModel(int reviewId, String message) {
        this.reviewId = reviewId;
        this.message = message;
    }
}
