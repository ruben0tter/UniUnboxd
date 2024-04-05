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

    /**
     * Constructor for the NotificationSettings class.
     *
     * @param StudentId                The student's ID.
     * @param ReceivesFollowersReviewMail Whether the student receives followers' review mail.
     * @param ReceivesFollowersReviewPush Whether the student receives followers' review push.
     * @param ReceivesNewReplyMail       Whether the student receives new reply mail.
     * @param ReceivesNewReplyPush       Whether the student receives new reply push.
     * @param ReceivesNewFollowerMail    Whether the student receives new follower mail.
     * @param ReceivesNewFollowerPush    Whether the student receives new follower push.
     */
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

