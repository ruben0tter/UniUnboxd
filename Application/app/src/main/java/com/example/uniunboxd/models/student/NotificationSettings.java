package com.example.uniunboxd.models.student;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationSettings {
    public int StudentId;
    public boolean ReceivesFollowersReviewMail;
    public boolean ReceivesFollowersReviewPush;
    public boolean ReceivesNewReplyMail;
    public boolean ReceivesNewReplyPush;
    public boolean ReceivesNewFollowerMail;
    public boolean ReceivesNewFollowerPush;

    @JsonCreator
    public NotificationSettings(@JsonProperty("studentId") int StudentId,
                                @JsonProperty("receivesFollowersReviewMail") boolean ReceivesFollowersReviewMail,
                                @JsonProperty("receivesFollowersReviewPush") boolean ReceivesFollowersReviewPush,
                                @JsonProperty("receivesNewReplyMail") boolean ReceivesNewReplyMail,
                                @JsonProperty("receivesNewReplyPush") boolean ReceivesNewReplyPush,
                                @JsonProperty("receivesNewFollowerMail") boolean ReceivesNewFollowerMail,
                                @JsonProperty("receivesNewFollowerPush") boolean ReceivesNewFollowerPush) {
        this.StudentId = StudentId;
        this.ReceivesFollowersReviewMail = ReceivesFollowersReviewMail;
        this.ReceivesFollowersReviewPush = ReceivesFollowersReviewPush;
        this.ReceivesNewReplyMail = ReceivesNewReplyMail;
        this.ReceivesNewReplyPush = ReceivesNewReplyPush;
        this.ReceivesNewFollowerMail = ReceivesNewFollowerMail;
        this.ReceivesNewFollowerPush = ReceivesNewFollowerPush;
    }
}

