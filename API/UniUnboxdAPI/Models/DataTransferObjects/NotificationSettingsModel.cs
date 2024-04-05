namespace UniUnboxdAPI.Models.DataTransferObjects;

public class NotificationSettingsModel
{
    public required int StudentId { get; set; }
    public bool ReceivesFollowersReviewMail { get; set; } = true;
    public bool ReceivesFollowersReviewPush { get; set; } = true;
    public bool ReceivesNewReplyMail { get; set; } = true;
    public bool ReceivesNewReplyPush { get; set; } = true;
    public bool ReceivesNewFollowerMail { get; set; } = true;
    public bool ReceivesNewFollowerPush { get; set; } = true;
}