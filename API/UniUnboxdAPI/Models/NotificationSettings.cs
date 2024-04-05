// This file contains the definition of the NotificationSettings class.
namespace UniUnboxdAPI.Models
{
    // The NotificationSettings class represents the notification settings for a student.
    public class NotificationSettings
    {
        // The ID of the student.
        public int StudentId { get; set; }

        // The student associated with the notification settings.
        public Student? Student { get; set; }

        // Indicates whether the student receives review emails from followers.
        public bool ReceivesFollowersReviewMail { get; set; } = true;

        // Indicates whether the student receives review push notifications from followers.
        public bool ReceivesFollowersReviewPush { get; set; } = true;

        // Indicates whether the student receives new reply emails.
        public bool ReceivesNewReplyMail { get; set; } = true;

        // Indicates whether the student receives new reply push notifications.
        public bool ReceivesNewReplyPush { get; set; } = true;

        // Indicates whether the student receives new follower emails.
        public bool ReceivesNewFollowerMail { get; set; } = true;

        // Indicates whether the student receives new follower push notifications.
        public bool ReceivesNewFollowerPush { get; set; } = true;
    }
}
