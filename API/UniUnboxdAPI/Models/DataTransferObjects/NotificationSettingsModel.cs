namespace UniUnboxdAPI.Models.DataTransferObjects;

public class NotificationSettingsModel
{
    public required int StudentId { get; set; }
    public required bool ReceivesFollowersReviewMail { get; set; } = true;
    public required bool ReceivesFollowersReviewPush { get; set; } = true;
    public required bool ReceivesNewReplyMail { get; set; } = true;
    public required bool ReceivesNewReplyPush { get; set; } = true;
    public required bool ReceivesNewFollowerMail { get; set; } = true;
    public required bool ReceivesNewFollowerPush { get; set; } = true;
}