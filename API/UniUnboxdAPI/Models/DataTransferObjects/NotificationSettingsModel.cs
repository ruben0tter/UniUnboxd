// NotificationSettingsModel class represents the data transfer object for notification settings.
namespace UniUnboxdAPI.Models.DataTransferObjects;

public class NotificationSettingsModel
{
    // StudentId represents the ID of the student.
    public required int StudentId { get; set; }

    // ReceivesFollowersReviewMail indicates whether the student receives mail notifications for followers' reviews.
    public bool ReceivesFollowersReviewMail { get; set; } = true;

    // ReceivesFollowersReviewPush indicates whether the student receives push notifications for followers' reviews.
    public bool ReceivesFollowersReviewPush { get; set; } = true;

    // ReceivesNewReplyMail indicates whether the student receives mail notifications for new replies.
    public bool ReceivesNewReplyMail { get; set; } = true;

    // ReceivesNewReplyPush indicates whether the student receives push notifications for new replies.
    public bool ReceivesNewReplyPush { get; set; } = true;

    // ReceivesNewFollowerMail indicates whether the student receives mail notifications for new followers.
    public bool ReceivesNewFollowerMail { get; set; } = true;

    // ReceivesNewFollowerPush indicates whether the student receives push notifications for new followers.
    public bool ReceivesNewFollowerPush { get; set; } = true;
}