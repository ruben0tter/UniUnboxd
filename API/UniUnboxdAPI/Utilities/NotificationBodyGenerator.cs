﻿using Microsoft.IdentityModel.Tokens;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPI.Utilities
{
    public static class NotificationBodyGenerator
    {
        public static string WelcomeStudentNotificationBody()
            => "Welcome to UniUnboxd! We are glad to have you.\n" +
            "In case you want to write your own reviews, make sure you head to the Profile Edit Page to verify your enrollment at your university.\n" +
            "We are looking forward to reading about your experiences! :)";

        public static string WelcomeUniversityNotificationBody()
            => "Welcome to UniUnboxd! We are glad to have you.\n" +
            "Make sure to head to the home page where you will encounter the \"Add Course +\" button.\n" +
            "Click on it to start the UniUnboxd journey right away!";

        public static string WelcomeProfessorNotificationBody()
            => "Welcome to UniUnboxd! We are glad to have you.\n" +
            "Once your university assign you to your courses, head to your Profile Page.\n" +
            "Here you will see an overview of your courses, click on them to Navigate towards the reviews.\n" +
            "We at UniUnbocd hope you will be able to help your students into a more pleasant university experience! :)";

        public static string NewFollowerNotificationBody(string name)
            => "Good news :), you have a new follower! Say hello to " + name;

        public static string NewReviewNotificationBody(string studentName, string courseName)
            => studentName + " has posted a review for the course " + courseName + ". Go check it out :)";

        public static string NewReplyNotificationBody(string userName, string courseName)
            => userName + " has replied to you review for the course " + courseName;

        public static string FlagReviewNotificationBody(FlagReviewModel model, int userId)
            => "The review with ID " + model.ReviewId + " has been flagged by a student with ID " + userId + ".\n" 
            + (model.Message.IsNullOrEmpty() ? string.Empty : ("The following message was given: \"" + model.Message + "\""));

        public static string VerificationStatusChangeBody(VerificationStatus status)
            => "Your verification application has been " + 
            (status == VerificationStatus.Verified 
            ? "approved.\n" + "Check out UniUnboxd for your newly unlocked features!" 
            : "rejected.");
    }
}
