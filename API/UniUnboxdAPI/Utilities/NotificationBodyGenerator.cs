using Microsoft.IdentityModel.Tokens;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPI.Utilities
{
    /// <summary>
    /// Generates notification bodies for different types of user notifications.
    /// </summary>
    public static class NotificationBodyGenerator
    {
        /// <summary>
        /// Generates a welcome message for new student users.
        /// </summary>
        /// <returns>A welcome message string.</returns>
        public static string WelcomeStudentNotificationBody()
            => "Welcome to UniUnboxd! We are glad to have you.\n" +
            "In case you want to write your own reviews, make sure you head to the Profile Edit Page to verify your enrollment at your university.\n" +
            "We are looking forward to reading about your experiences! :)";

        /// <summary>
        /// Generates a welcome message for new university users.
        /// </summary>
        /// <returns>A welcome message string.</returns>
        public static string WelcomeUniversityNotificationBody()
            => "Welcome to UniUnboxd! We are glad to have you.\n" +
            "Make sure to head to the home page where you will encounter the \"Add Course +\" button.\n" +
            "Click on it to start the UniUnboxd journey right away!";

        /// <summary>
        /// Generates a welcome message for new professor users.
        /// </summary>
        /// <returns>A welcome message string.</returns>
        public static string WelcomeProfessorNotificationBody()
            => "Welcome to UniUnboxd! We are glad to have you.\n" +
            "Once your university assign you to your courses, head to your Profile Page.\n" +
            "Here you will see an overview of your courses, click on them to Navigate towards the reviews.\n" +
            "We at UniUnbocd hope you will be able to help your students into a more pleasant university experience! :)";

        /// <summary>
        /// Generates a notification body when a user gets a new follower.
        /// </summary>
        /// <param name="name">The name of the new follower.</param>
        /// <returns>A notification message string.</returns>
        public static string NewFollowerNotificationBody(string name)
            => "Good news :), you have a new follower! Say hello to " + name;

        /// <summary>
        /// Generates a notification body when a new review is posted for a course.
        /// </summary>
        /// <param name="studentName">The name of the student who posted the review.</param>
        /// <param name="courseName">The name of the course reviewed.</param>
        /// <returns>A notification message string.</returns>
        public static string NewReviewNotificationBody(string studentName, string courseName)
            => studentName + " has posted a review for the course " + courseName + ". Go check it out :)";

        /// <summary>
        /// Generates a notification body when a user replies to a review.
        /// </summary>
        /// <param name="userName">The name of the user who replied.</param>
        /// <param name="courseName">The name of the course related to the review.</param>
        /// <returns>A notification message string.</returns>
        public static string NewReplyNotificationBody(string userName, string courseName)
            => userName + " has replied to your review for the course " + courseName;

        /// <summary>
        /// Generates a notification body when a review is flagged.
        /// </summary>
        /// <param name="model">The flag review model containing details about the flagging.</param>
        /// <param name="userId">The ID of the user who flagged the review.</param>
        /// <returns>A notification message string.</returns>
        public static string FlagReviewNotificationBody(FlagReviewModel model, int userId)
            => "The review with ID " + model.ReviewId + " has been flagged by a student with ID " + userId + ".\n"
            + (model.Message.IsNullOrEmpty() ? string.Empty : ("The following message was given: \"" + model.Message + "\""));

        /// <summary>
        /// Generates a notification body for changes in verification status.
        /// </summary>
        /// <param name="status">The new verification status.</param>
        /// <returns>A notification message string reflecting the new status.</returns>
        public static string VerificationStatusChangeBody(VerificationStatus status)
            => "Your verification application has been " +
            (status == VerificationStatus.Verified
            ? "approved.\n" + "Check out UniUnboxd for your newly unlocked features!"
            : "rejected.");
    }
}
