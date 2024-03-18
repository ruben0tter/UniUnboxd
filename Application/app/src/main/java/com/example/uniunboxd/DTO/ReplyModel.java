package com.example.uniunboxd.DTO;

public class ReplyModel {
    public final String text;
    public final int reviewId;

    public ReplyModel(String text, int reviewId) {
        this.text = text;
        this.reviewId = reviewId;
    }
}
