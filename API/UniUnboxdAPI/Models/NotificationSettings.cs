namespace UniUnboxdAPI.Models
{
    public class NotificationSettings
    {
        public int StudentId { get; set; }
        public Student? Student { get; set; }
        public bool ReceivesVerificationStatusChangeMail { get; set; } = true;
        public bool ReceivesVerificationStatusChangePush { get; set; } = true;
        public bool ReceivesFollowersReviewMail { get; set; } = true;
        public bool ReceivesFollowersReviewPush { get; set; } = true;
        public bool ReceivesNewReplyMail { get; set; } = true;
        public bool ReceivesNewReplyPush { get; set; } = true;
        public bool ReceivesNewFollowerMail { get; set; } = true;
        public bool ReceivesNewFollowerPush { get; set; } = true;
    }
}
